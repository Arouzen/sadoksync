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
 * @author Pontus
 */
public class StreamThread extends Thread {

    PublicPlaylist playlist;
    HeadlessMediaPlayer serverMediaPlayer;
    MediaPlayerFactory serverMediaPlayerFactory;
    Client client;

    public StreamThread(PublicPlaylist playlist, Client client) {
        this.playlist = playlist;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            System.out.println("[MediaServer.startStreamingServer] Starting thread");
            Media media = playlist.getFirstInList();
            //No ip address here, only an @. 

            serverMediaPlayerFactory = new MediaPlayerFactory();
            serverMediaPlayer = serverMediaPlayerFactory.newHeadlessMediaPlayer();

            serverMediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

                @Override
                public void finished(MediaPlayer serverMediaPlayer) {
                    System.out.println("Event: Finished");
                    mediaEnded(serverMediaPlayer);
                    /*
                     if (playlist.getFirstInList().type.equals("youtube")) {
                     // This is key...
                     //
                     // On receipt of a "finished" event, check if sub-items have been created...
                     List<String> subItems = serverMediaPlayer.subItems();
                     System.out.println("subItems=" + subItems);
                     // If sub-items were created...
                     if (subItems == null || subItems.isEmpty()) {
                     System.out.println("utube done? maybe");
                     mediaEnded(serverMediaPlayer);
                     }
                     } else {
                     System.out.println("not utube");
                     mediaEnded(serverMediaPlayer);
                     }
                     */
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
                    mediaEnded(serverMediaPlayer);
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
                    String ip = playlist.getFirstInListOwner();

                    if (client.pr.getMyIp().equals(ip) /*|| !media.getType().equals("local file")*/) {
                        streamMedia(serverMediaPlayer, media.getPath(), media, "");
                    } else {
                        //mediaPlayer.release();
                        //serverMediaPlayerFactory.release();
                        client.pr.ping(ip, "Move Host");
                    }
                }
            });
            String p = media.getPath();
            streamMedia(serverMediaPlayer, p, media, "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*
     public void stopMedia() {
     System.out.println("StreamThread: stopMedia:");
     if (serverMediaPlayer != null) {
     System.out.println("StreamThread: stopMedia: 1");
     if (serverMediaPlayer.isPlaying()) {
     System.out.println("StreamThread: stopMedia: 2");
     //serverMediaPlayer.stop();
     System.out.println("StreamThread: stopMedia: 3");
     }
     }
     }
     */

    public void kill() {
        System.out.println("StreamThread: kill:");
        //this.stopMedia();
        System.out.println("StreamThread: kill: 1");
        if (serverMediaPlayer != null) {
            System.out.println("StreamThread: kill: 2");
            serverMediaPlayer.release();
            System.out.println("StreamThread: kill: 3");
        }
        if (serverMediaPlayerFactory != null) {
            System.out.println("StreamThread: kill: 4");
            serverMediaPlayerFactory.release();
            System.out.println("StreamThread: kill: 5");
        }
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

        //Let the stream startup
        try {
            Thread.sleep(1000);
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
        client.pr.deliverStreamToComunity(mediaType);
        client.pr.deliverPlaylistToComunity();
    }

    private void mediaEnded(MediaPlayer serverMediaPlayer) {
        //kill stream if we alway kill the strea and re-start it. always?
        kill();

        playlist.removeFirstInQueue();
        client.updateRightPanel(client.getPlaylist());
        client.rightPanelMode = "playlist";

        if (!playlist.isEmpty()) {
            String ip = playlist.getFirstInListOwner();
            if (!client.pr.getMyIp().equals(ip)) {
                //kill stream if we are reusing the stream
            }
            //Tests if we should start the next or if someone eles should.
            client.startStream();
            //streamNextMedia(client.mediaPlayer);

        } else {
            System.out.println("[Server] No more media in list");

            //kill stream if we are reusing the stream
        }
    }

    public void endMedia() {
        mediaEnded(serverMediaPlayer);
    }
}
