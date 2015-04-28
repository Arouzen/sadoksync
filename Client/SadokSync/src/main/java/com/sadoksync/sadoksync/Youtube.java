/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009, 2010, 2011, 2012, 2013, 2014, 2015 Caprica Software Limited.
 */
package com.sadoksync.sadoksync;

import com.sun.jna.NativeLibrary;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

/**
 * A minimal YouTube player.
 * <p>
 * The URL/MRL must be in the following format:
 *
 * <pre>
 *   http://www.youtube.com/watch?v=000000
 * </pre>
 *
 * The only thing that makes this different from a 'regular' media player
 * application is the following piece of code:
 *
 * <pre>
 * mediaPlayer.setPlaySubItems(true); // &lt;--- This is very important for YouTube media
 * </pre>
 *
 * Note that it is also possible to programmatically play the sub-item in
 * response to events - this is slightly more complex but more flexible.
 * <p>
 * The YouTube web page format changes from time to time. This means that the
 * lua scripts that vlc provides to parse the YouTube web pages when looking for
 * the media to stream may not work. If you get errors, especially errors
 * alluding to malformed urls, then you may need to update your vlc version to
 * get newer lua scripts.
 */
public class Youtube {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final JFrame frame;

    public static void main(String[] args) throws Exception {
        //VLCLibrary init
        StringBuilder location = new StringBuilder(Client.class.getProtectionDomain().getCodeSource().getLocation().toString());
        location.delete(0, 6);
        location.append("VLC/");
        System.out.println(location);
        NativeLibrary.addSearchPath("libvlc", location.toString());
        final String mrl = "https://www.youtube.com/watch?v=HHFFdy2NSdE";

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Youtube().start(mrl);
            }
        });

        Thread.currentThread().join();
    }

    public Youtube() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {

            @Override
            public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
                // Show the sub-item being added for purposes of the test...
                System.out.println("mediaSubItemAdded: " + mediaPlayerComponent.getMediaPlayer().mrl(subItem));
                List<String> subItems = mediaPlayer.subItems();
                System.out.println("subItems=" + subItems);
                // If sub-items were created...
                if(subItems != null && !subItems.isEmpty()) {
                    // Pick the first sub-item, and play it...
                    String subItemMrl = subItems.get(0);
                    mediaPlayer.playMedia(subItemMrl);
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
                else {
                    // Your own application would not exit, but would instead probably set some
                    // state flag or fire some sort of signal or whatever that the media actually
                    // finished
                    //System.exit(0);
                }
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("finished");

                // This is key...
                //
                // On receipt of a "finished" event, check if sub-items have been created...
                List<String> subItems = mediaPlayer.subItems();
                System.out.println("subItems=" + subItems);
                // If sub-items were created...
                if(subItems != null && !subItems.isEmpty()) {
                    // Pick the first sub-item, and play it...
                    String subItemMrl = subItems.get(0);
                    mediaPlayer.playMedia(subItemMrl);
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
                else {
                    // Your own application would not exit, but would instead probably set some
                    // state flag or fire some sort of signal or whatever that the media actually
                    // finished
                    //System.exit(0);
                }
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                // For some reason, even if things work, you get an error... you have to ignore
                // this error - but that of course makes handling real errors tricky
                System.out.println("Error!!!");
            }
        };

        frame = new JFrame("vlcj YouTube fallback test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 1200, 800);
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
    }

    private void start(String mrl) {
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }
}
