package com.sadoksync.sadoksync;

import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.FileDialog;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;

/**
 *
 * @author Arouz
 */
public class Client extends javax.swing.JFrame {

    // Create a media player factory
    private final MediaPlayerFactory mediaPlayerFactory;

    // Create a new media player instance for the run-time platform
    public EmbeddedMediaPlayer mediaPlayer;

    // Create video surface
    public CanvasVideoSurface videoSurface;

    // Create fullscreen player
    private final FullScreenPlayer fullscreenplayer;

    /**
     * Creates new form Client
     */
    public Client() {
        initComponents();

        //VLCLibrary init
        StringBuilder location = new StringBuilder(Client.class.getProtectionDomain().getCodeSource().getLocation().toString());
        location.delete(0, 6);
        location.append("VLC/");
        System.out.println(location);
        NativeLibrary.addSearchPath("libvlc", location.toString());

        //Fullscreenplayer init
        fullscreenplayer = new FullScreenPlayer();

        //Media player init
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(fullscreenplayer.frame));

        videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        panelChatt = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        buttonPlay = new javax.swing.JButton();
        textFileLocation = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        ButtonPause = new javax.swing.JButton();
        ButtonStop = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        canvas = new java.awt.Canvas();
        scrollPaneChatt = new javax.swing.JScrollPane();
        textarea = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        Open = new javax.swing.JMenuItem();
        Exit = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(82, 68, 68));

        jButton2.setText("jButton2");

        jTextField1.setText("jTextField1");

        javax.swing.GroupLayout panelChattLayout = new javax.swing.GroupLayout(panelChatt);
        panelChatt.setLayout(panelChattLayout);
        panelChattLayout.setHorizontalGroup(
            panelChattLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChattLayout.createSequentialGroup()
                .addComponent(jTextField1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelChattLayout.setVerticalGroup(
            panelChattLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChattLayout.createSequentialGroup()
                .addGroup(panelChattLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("S A D O K S Y N C !");

        jButton1.setText("Server");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_play_Default.png"))); // NOI18N
        buttonPlay.setMaximumSize(new java.awt.Dimension(85, 40));
        buttonPlay.setMinimumSize(new java.awt.Dimension(85, 40));
        buttonPlay.setPreferredSize(new java.awt.Dimension(85, 40));
        buttonPlay.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_play_Clicked.png"))); // NOI18N
        buttonPlay.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_play_Default1.png"))); // NOI18N
        buttonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlayActionPerformed(evt);
            }
        });

        jButton3.setText("fullscreen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        ButtonPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_Default.png"))); // NOI18N
        ButtonPause.setMaximumSize(new java.awt.Dimension(85, 40));
        ButtonPause.setMinimumSize(new java.awt.Dimension(85, 40));
        ButtonPause.setPreferredSize(new java.awt.Dimension(85, 40));
        ButtonPause.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/copy.png"))); // NOI18N
        ButtonPause.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_Hover.png"))); // NOI18N
        ButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPauseActionPerformed(evt);
            }
        });

        ButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_Stop_default.png"))); // NOI18N
        ButtonStop.setMaximumSize(new java.awt.Dimension(85, 40));
        ButtonStop.setMinimumSize(new java.awt.Dimension(85, 40));
        ButtonStop.setPreferredSize(new java.awt.Dimension(85, 40));
        ButtonStop.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_Stop_Hover.png"))); // NOI18N
        ButtonStop.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Button_Stop_Click.png"))); // NOI18N
        ButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonStopActionPerformed(evt);
            }
        });

        jButton4.setText("Start Streaming");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Show Users");

        jButton6.setText("Show Chat");

        canvas.setMinimumSize(new java.awt.Dimension(590, 484));
        jSplitPane1.setLeftComponent(canvas);

        textarea.setEditable(false);
        textarea.setBackground(new java.awt.Color(102, 102, 102));
        textarea.setColumns(20);
        textarea.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        textarea.setForeground(new java.awt.Color(255, 255, 255));
        textarea.setRows(5);
        scrollPaneChatt.setViewportView(textarea);

        jSplitPane1.setRightComponent(scrollPaneChatt);

        jMenu1.setText("File");

        Open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Open.setText("Open");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        jMenu1.add(Open);

        Exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GeoGebra_icon_exit.png"))); // NOI18N
        Exit.setText("Exit");
        Exit.setMaximumSize(new java.awt.Dimension(320, 320));
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        jMenu1.add(Exit);
        jMenu1.add(jSeparator2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem1.setText("Help Contents");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);
        jMenu2.add(jSeparator1);

        jMenuItem2.setText("About");
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textFileLocation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addGap(9, 9, 9))
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonPause, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelChatt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)))))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelChatt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textFileLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4))
                    .addComponent(buttonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonPause, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        FileDialog fileDialog;
        fileDialog = new FileDialog(this);
        fileDialog.setVisible(true);
        File[] returnVal = fileDialog.getFiles();
        if (returnVal[0] != null) {
            textFileLocation.setText(returnVal[0].getAbsolutePath());
        }
    }//GEN-LAST:event_OpenActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_ExitActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new HelpContentsGUI().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new ServerNameGUI().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlayActionPerformed
        if (!textFileLocation.getText().isEmpty()) {
            mediaPlayer.playMedia(textFileLocation.getText());
        }
    }//GEN-LAST:event_buttonPlayActionPerformed

    private void ButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPauseActionPerformed
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
    }//GEN-LAST:event_ButtonPauseActionPerformed

    private void ButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonStopActionPerformed
        mediaPlayer.stop();
    }//GEN-LAST:event_ButtonStopActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (!textFileLocation.getText().isEmpty()) {
            String[] arbeit = new String[1];
            arbeit[0] = textFileLocation.getText();
            try {
                StreamRtsp.main(arbeit);
            } catch (Exception ex) {
                System.out.println("No please");
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (mediaPlayer.isPlaying()) {
            fullscreenplayer.fullscreen();
            mediaPlayer.setFullScreen(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //VLC now included in project instead
        //NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    class FullScreenPlayer {

        public Canvas canvas;
        public JFrame frame;
        public CanvasVideoSurface videoSurface;

        private final KeyStroke escapeKeyStroke;
        private final Action escapeAction;

        private FullScreenPlayer() {
            frame = new JFrame();
            canvas = new Canvas();
            frame.setLocation(100, 100);

            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = gd.getDisplayMode().getWidth();
            int height = gd.getDisplayMode().getHeight();
            frame.setSize(width, height);
            canvas.setSize(width, height);
            frame.add(canvas);
            escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
            escapeAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    stopFullscreen();
                }
            };
            frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
            frame.getRootPane().getActionMap().put("ESCAPE", escapeAction);
        }

        private void fullscreen() {
            this.videoSurface = mediaPlayerFactory.newVideoSurface(this.canvas);
            frame.setVisible(true);
            canvas.setVisible(true);

            // Workaround, disable videotrack while swapping surface
            int vid = mediaPlayer.getVideoTrack();
            mediaPlayer.setVideoTrack(-1);

            mediaPlayer.setVideoSurface(this.videoSurface);
            mediaPlayer.attachVideoSurface();

            // Put videotrack back
            mediaPlayer.setVideoTrack(vid);

        }

        private void stopFullscreen() {
            // Workaround, disable videotrack while swapping surface
            int vid = mediaPlayer.getVideoTrack();
            mediaPlayer.setVideoTrack(-1);

            mediaPlayer.setVideoSurface(Client.this.videoSurface);
            mediaPlayer.attachVideoSurface();

            // Put videotrack back
            mediaPlayer.setVideoTrack(vid);

            frame.setVisible(false);
            canvas.setVisible(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonPause;
    private javax.swing.JButton ButtonStop;
    private javax.swing.JMenuItem Exit;
    private javax.swing.JMenuItem Open;
    private javax.swing.JButton buttonPlay;
    private java.awt.Canvas canvas;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panelChatt;
    private javax.swing.JScrollPane scrollPaneChatt;
    private javax.swing.JTextField textFileLocation;
    private javax.swing.JTextArea textarea;
    // End of variables declaration//GEN-END:variables
}