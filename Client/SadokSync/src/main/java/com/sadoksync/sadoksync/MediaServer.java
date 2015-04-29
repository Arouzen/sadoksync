/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

/**
 *
 * @author Arouz
 */
public class MediaServer {

    PublicPlaylist playlist;
    HeadlessMediaPlayer serverMediaPlayer;
    MediaPlayerFactory serverMediaPlayerFactory;
    Thread streamingServer;
    Client client;

    public MediaServer(PublicPlaylist playlist, Client client) {
        this.playlist = playlist;
        this.client = client;
    }

    public void stop() {
        if (serverMediaPlayer.isPlaying()) {
            serverMediaPlayer.stop();
        }
    }

    public void kill(boolean release) {
        System.out.println("[MediaServer.kill] Kill start");
        //
        if (release) {
            serverMediaPlayer.release();
        }
        //serverMediaPlayerFactory.release();
        streamingServer.stop();
        System.out.println("[MediaServer.kill] Kill finish");
    }

    public void streamMedia(MediaPlayer serverMediaPlayer, String mrl, Media media, String mediaType) {
        final String options = formatRtspStream("@", 5555, "demo");
        System.out.println("[Client.streamMedia] Streaming '" + mrl + "' to '" + options + "'");
        serverMediaPlayer.playMedia(mrl,
                options,
                ":no-sout-rtp-sap",
                ":no-sout-standard-sap",
                ":sout-all",
                ":sout-keep"
        );
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        client.setHost("localhost");
        client.setPort("5555");
        client.setRtspPath("demo");

        if (mediaType.equals("")) {
            if (media.getType().equals("local file")) {
                String extension = media.getPath().split("\\.")[media.getPath().split("\\.").length - 1];
                try {
                    if (client.filefilter.acceptMediaFile(extension, "visualize")) {
                        mediaType = "visualize";
                    } else if (client.filefilter.acceptMediaFile(extension, "video")) {
                        mediaType = "video";
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                mediaType = media.getType();
            }
        }

        client.setMediaType(mediaType);
        client.playMedia(client.getRtspUrl());
        client.pr.DeliverStreamToComunity(client.pr.getMyIp(), "demo", mediaType);
        client.pr.DeliverPlaylistToComunity();
    }

    private static String formatRtspStream(String serverAddress, int serverPort, String id) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#rtp{sdp=rtsp://@");
        sb.append(serverAddress);
        sb.append(':');
        sb.append(serverPort);
        sb.append('/');
        sb.append(id);
        sb.append("}");
        return sb.toString();
    }

    void startStreamingServer() {
        try {
            streamingServer = new Thread() {
                public void run() {
                    try {
                        System.out.println("[MediaServer.startStreamingServer] Starting thread");
                        Media media = playlist.getFirstInList();
                        //No ip address here, only an @. 

                        serverMediaPlayerFactory = new MediaPlayerFactory();
                        serverMediaPlayer = serverMediaPlayerFactory.newHeadlessMediaPlayer();

                        serverMediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                            public void mediaStoppedFinished(MediaPlayer serverMediaPlayer, boolean release) {
                                playlist.removeFirstInQueue();
                                client.updateRightPanel(client.getPlaylist());
                                client.rightPanelMode = "playlist";
                                /*if (!playlist.isEmpty()) {
                                 String ip = client.pr.com.getPeerIP(playlist.getFirstInListOwner());
                                 if (!client.pr.getMyIp().equals(ip)) {
                                 if (release) {
                                 serverMediaPlayer.release();
                                 }
                                 }

                                 streamNextMedia(client.mediaPlayer);

                                 } else {
                                 System.out.println("[Server] No more media in list");
                                 if (release) {
                                 serverMediaPlayer.release();
                                 }
                                 //serverMediaPlayerFactory.release();
                                 }*/
                                client.startStream();
                                kill(release);
                            }

                            @Override
                            public void finished(MediaPlayer serverMediaPlayer) {
                                System.out.println("Event: Finished");
                                if (playlist.getFirstInList().type.equals("youtube")) {
                                    // This is key...
                                    //
                                    // On receipt of a "finished" event, check if sub-items have been created...
                                    List<String> subItems = serverMediaPlayer.subItems();
                                    System.out.println("subItems=" + subItems);
                                    // If sub-items were created...
                                    if (subItems == null || subItems.isEmpty()) {
                                        System.out.println("utube done? maybe");
                                        mediaStoppedFinished(serverMediaPlayer, true);
                                    }
                                } else {
                                    System.out.println("not utube");
                                    mediaStoppedFinished(serverMediaPlayer, true);
                                }
                            }

                            @Override
                            public void error(MediaPlayer mediaPlayer) {
                                // For some reason, even if things work, you get an error... you have to ignore
                                // this error - but that of course makes handling real errors tricky
                                System.out.println("Error!!!");
                            }

                            @Override
                            public void stopped(MediaPlayer serverMediaPlayer) {
                                System.out.println("Event: Stopped");
                                mediaStoppedFinished(serverMediaPlayer, false);
                            }

                            @Override
                            public void mediaSubItemAdded(MediaPlayer serverMediaPlayer, libvlc_media_t subItem) {
                                List<String> subItems = serverMediaPlayer.subItems();
                                System.out.println("subItems=" + subItems);
                                // If sub-items were created...
                                if (subItems != null && !subItems.isEmpty()) {
                                    // Pick the first sub-item, and play it...
                                    String subItemMrl = subItems.get(0);
                                    streamMedia(serverMediaPlayer, subItemMrl, null, "youtube");
                                    // What will happen next...
                                    //
                                    // 1. if the vlc lua script finds the streaming MRL via the normal i.e.
                                    //    "primary" method, then this subitem MRL will be the streaming MRL; or
                                    // 2. if the vlc lua script does not find the streaming MRL via the primary
                                    //    method, then the vlc lua script fallback method is tried to locate the
                                    //    streaming MRL and the next time a "finished" event is received there will
                                    //    be a new sub-item for the just played subitem, and that will be the
                                    //    streaming MRL
                                }
                            }

                            @Override
                            public void buffering(MediaPlayer mediaPlayer, float newCache) {
                                //System.out.println("Buffering " + newCache);
                            }

                            private void streamNextMedia(MediaPlayer serverMediaPlayer) {
                                System.out.println("[Server] Media stopped/finished, moving next in list!");

                                //get the next media object in list
                                Media media = playlist.getFirstInList();

                                //get the ip of the owner of the next media.
                                String ip = client.pr.com.getPeerIP(playlist.getFirstInListOwner());

                                if (client.pr.getMyIp().equals(ip) /*|| !media.getType().equals("local file")*/) {
                                    streamMedia(serverMediaPlayer, media.getPath(), media, "");
                                } else {
                                    //mediaPlayer.release();
                                    //serverMediaPlayerFactory.release();
                                    client.pr.Ping(ip, "Move Host");
                                }
                            }
                        });

                        streamMedia(serverMediaPlayer, media.getPath(), media, "");

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            streamingServer.start();
        } catch (Exception ex) {
            System.out.println("No please");
        }
    }
}
