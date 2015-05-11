package com.sadoksync.sadoksync;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

/**
 *
 * @author Pontus
 */
public class StreamThread extends Thread {

    DebugSys dbs;
    PublicPlaylist playlist;
    HeadlessMediaPlayer serverMediaPlayer;
    MediaPlayerFactory serverMediaPlayerFactory;
    Client client;
    MediaListPlayer mlp;
    boolean finishedOnce;

    public StreamThread(PublicPlaylist playlist, Client client, DebugSys dbs) {
        this.playlist = playlist;
        this.client = client;
        this.dbs = dbs;
        this.finishedOnce = false;
    }

    @Override
    public void run() {
        try {
            dbs.println("[MediaServer.startStreamingServer] Starting thread");
            //System.out.println("[MediaServer.startStreamingServer] Starting thread");
            final Media media = playlist.getFirstInList();
            //No ip address here, only an @. 

            serverMediaPlayerFactory = new MediaPlayerFactory();
            serverMediaPlayer = serverMediaPlayerFactory.newHeadlessMediaPlayer();

            serverMediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                @Override
                public void finished(MediaPlayer serverMediaPlayer) {
                    dbs.println("Event: Finished");
                    dbs.println("MEDIATYPE: " + media.getType());
                    dbs.println("MEDIAPATH: " + media.getPath());
                    // ignore if mediatype is youtube, and its the first finish call,
                    // cause youtube will always call finish once (falsly) then play for real. kinda
                    if (finishedOnce || !media.getType().equals("youtube")) {
                        mediaEnded(serverMediaPlayer);
                    } else {
                        dbs.println("FIRST FINISH.");
                        finishedOnce = true;
                    }
                }

                @Override
                public void stopped(MediaPlayer serverMediaPlayer) {
                    dbs.println("Event: Stopped");
                    mediaEnded(serverMediaPlayer);
                }
            });

            if (media.getType().equals("youtube")) {

                mlp = serverMediaPlayerFactory.newMediaListPlayer();
                mlp.setMediaPlayer(serverMediaPlayer);
                MediaList mediaList = serverMediaPlayerFactory.newMediaList();

                final String options = formatRtspStream("@", 1024, "demo");

                mediaList.addMedia(media.getPath(),
                        options,
                        //":sout-keep",
                        //":sout-avcodec-keyint=10",
                        ":no-sout-rtp-sap",
                        ":no-sout-standard-sap",
                        ":sout-all",
                        ":sout-keep"
                );

                mlp.setMediaList(mediaList);
                mlp.play();

                dbs.println("[Client.streamMedia YOUTUBE] Streaming '" + media.getPath() + "' to '" + options + "'");

                //Let the stream startup
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

                client.setHost("localhost");
                client.setPort("1024");
                client.setRtspPath("demo");

                client.setMediaType("youtube");

                client.connectToRtsp();
                client.pr.DeliverStreamToComunity(client.pr.getMyIp(), "demo", "youtube");
                client.pr.DeliverPlaylistToComunity();

            } else {

                streamMedia(serverMediaPlayer, media.getPath(), media, "");
            }
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
        dbs.println("StreamThread: kill:");
        //System.out.println("StreamThread: kill:");
        //this.stopMedia();
        //System.out.println("StreamThread: kill: 1");
        if (serverMediaPlayer != null) {
            //System.out.println("StreamThread: kill: 2");
            serverMediaPlayer.release();
            //System.out.println("StreamThread: kill: 3");
        }
        if (serverMediaPlayerFactory != null) {
            //System.out.println("StreamThread: kill: 4");
            serverMediaPlayerFactory.release();
            //System.out.println("StreamThread: kill: 5");
        }
        if (mlp != null) {
            mlp.release();
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
        final String options = formatRtspStream("@", 1024, "demo");
        dbs.println("[Client.streamMedia] Streaming '" + mrl + "' to '" + options + "'");
        //System.out.println("[Client.streamMedia] Streaming '" + mrl + "' to '" + options + "'");
        serverMediaPlayer.playMedia(mrl,
                options,
                //":sout-keep",
                //":sout-avcodec-keyint=10",
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
        client.setPort("1024");
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

        System.out.println("TYPE: " + mediaType);
        client.setMediaType(mediaType);

        client.connectToRtsp();
        client.pr.DeliverStreamToComunity(client.pr.getMyIp(), "demo", mediaType);
        client.pr.DeliverPlaylistToComunity();
    }

    private void mediaEnded(MediaPlayer serverMediaPlayer) {
        //kill stream if we alway kill the strea and re-start it. always?
        kill();

        playlist.removeFirstInQueue();
        client.updateRightPanel(client.getPlaylist());
        client.rightPanelMode = "playlist";

        if (!playlist.isEmpty()) {
            String ip = client.pr.com.getPeerIP(playlist.getFirstInListOwner());
            if (!client.pr.getMyIp().equals(ip)) {
                //kill stream if we are reusing the stream
            }
            //Tests if we should start the next or if someone eles should.
            client.startStream();
            //streamNextMedia(client.mediaPlayer);

        } else {
            dbs.println("[Server] No more media in list");
            //System.out.println("[Server] No more media in list");

            //kill stream if we are reusing the stream
        }
    }

    public void endMedia() {
        mediaEnded(serverMediaPlayer);
    }
}
