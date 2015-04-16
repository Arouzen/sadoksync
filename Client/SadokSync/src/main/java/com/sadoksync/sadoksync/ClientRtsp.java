/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sun.jna.NativeLibrary;
import java.io.File;
import javax.swing.JFrame;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 *
 * @author Skogsfaktor
 */
public class ClientRtsp {

    public static void main(String[] args) {
        StringBuilder filePath = new StringBuilder();
        filePath.append(new File("").getAbsolutePath());
        filePath.append("\\target\\VLC64");
        System.out.println(filePath);
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), filePath.toString());

        JFrame frame = new JFrame();
        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setSize(1050, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@127.0.0.1:5555/demo");

    }
//        String options = formatRtspStream("127.0.0.1", 5555, "demo");
//        String media = "rtsp://@127.0.0.1:5555/demo";
////        System.out.println("Streaming '" + media + "' to '" + options + "'");
////        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
////        HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
////        mediaPlayer.playMedia(media,
////                options,
////                ":no-sout-rtp-sap",
////                ":no-sout-standard-sap",
////                ":sout-all",
////                ":sout-keep"
////        );
//
//        mediaPlayerComponent.getMediaPlayer().playMedia(media,
//                options,
//                ":no-sout-rtp-sap",
//                ":no-sout-standard-sap",
//                ":sout-all",
//                ":sout-keep");
//
////       mediaPlayerComponent.getMediaPlayer().playMedia("rtsp://127.0.0.1:5555/demo", 
////                    ":sout=#transcode{vcodec=h264,vb=800,fps=15,scale=1,width=1280,height=800,acodec=mp4a,ab=128,channels=2,samplerate=44100}:rtp{sdp=rtsp://@:5555/demo}",
////                      ":no-sout-rtp-sap", 
////                     ":no-sout-standard-sap", 
////                      ":sout-all", 
////                      ":sout-keep");
////        HeadlessMediaPlayer player = new HeadlessMediaPlayer();
//    }
//
//    private static String formatRtspStream(String serverAddress, int serverPort, String id) {
//        StringBuilder sb = new StringBuilder(60);
//        sb.append(":sout=#rtp{sdp=rtsp://@");
//        sb.append(serverAddress);
//        sb.append(':');
//        sb.append(serverPort);
//        sb.append('/');
//        sb.append(id);
//        sb.append("}");
//        return sb.toString();
//    }

}
