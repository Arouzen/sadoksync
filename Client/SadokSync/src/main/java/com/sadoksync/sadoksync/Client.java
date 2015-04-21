package com.sadoksync.sadoksync;

import com.sadoksync.sadoksync.PublicPlaylist.Pair;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

/**
 *
 * @author Arouz
 */
public class Client extends javax.swing.JFrame {

    // Create a media player factory
    private final MediaPlayerFactory mediaPlayerFactory;

    // Create a new media player instance for the run-time platform
    public EmbeddedMediaPlayer mediaPlayer;

    // Create the server media player used to stream
    public HeadlessMediaPlayer serverMediaPlayer;

    // Create video surface
    public CanvasVideoSurface videoSurface;

    // Create fullscreen player
    private final FullScreenPlayer fullscreenplayer;

    // Create the public playlist
    private PublicPlaylist playlist;

    // Create windows-look file chooser
    private JFileChooser fileChooser;

    // Rtsp variables
    private String server;
    private String port;
    private String rtspPath;

    private boolean isHost;
    private Peer pr;

    public ServerNameGUI serverNameGUI;
    private final String nick;

    /**
     * Creates new form Client
     *
     * @param pr Peer
     */
    public Client(Peer pr) {
        // Save java Look&Feel (L&F)
        LookAndFeel originalLaf = UIManager.getLookAndFeel();
        try {
            // Switch to Windows L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Create the Windows L&F file chooser
        fileChooser = new JFileChooser();

        try {
            //Flick the L&F back to the default
            UIManager.setLookAndFeel(originalLaf);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Now create rest of components with saved default layout
        initComponents();

        this.pr = pr;
        this.nick = pr.getNick();

        //VLCLibrary init
        StringBuilder location = new StringBuilder(Client.class.getProtectionDomain().getCodeSource().getLocation().toString());
        location.delete(0, 6);
        location.append("VLC/");
        System.out.println(location);
        NativeLibrary.addSearchPath("libvlc", location.toString());

        //Server popup thingy (???) init
        serverNameGUI = new ServerNameGUI();

        //File chooser settings init
        fileChooser.setFileFilter(new FileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        //Fullscreenplayer init
        fullscreenplayer = new FullScreenPlayer();

        //Media player init
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(fullscreenplayer.frame));

        videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                if (!playlist.isEmpty()) {
                }
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                if (!playlist.isEmpty()) {
                }
            }
        });

        jSplitPane1.setDividerLocation(0.7);

        // Public playlist init
        playlist = new PublicPlaylist();
        /*
        this.isHost = pr.isHost();
        if (!isHost) {
            buttonStream.setEnabled(false);
        }
        */
    }

    public void persistClient(MediaPlayer mediaPlayer) {
        System.out.println("Playing next video after 5sec...(client)");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        mediaPlayer.playMedia(getRtspUrl());
    }

    public void setHost(String ip) {
        this.server = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setRtspPath(String path) {
        this.rtspPath = path;
    }

    public String getRtspUrl() {
        return "rtsp://" + this.server + ":" + this.port + "/" + this.rtspPath;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollListPanel = new javax.swing.JScrollPane();
        listInScrollpane = new javax.swing.JList();
        panelChatt = new javax.swing.JPanel();
        buttonSendChat = new javax.swing.JButton();
        textChatInput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        buttonPlay = new javax.swing.JButton();
        textFileLocation = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        ButtonPause = new javax.swing.JButton();
        ButtonStop = new javax.swing.JButton();
        buttonStream = new javax.swing.JButton();
        buttonShowUsers = new javax.swing.JButton();
        buttonShowChat = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        canvas = new java.awt.Canvas();
        scrollPaneChatt = new javax.swing.JScrollPane();
        textChatOutput = new javax.swing.JTextArea();
        buttonShowPlaylist = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        Open = new javax.swing.JMenuItem();
        Exit = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();

        DefaultListModel listModel = new DefaultListModel();
        listInScrollpane.setModel(listModel);
        scrollListPanel.setViewportView(listInScrollpane);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(82, 68, 68));

        buttonSendChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send_button_default _ off.png"))); // NOI18N
        buttonSendChat.setMaximumSize(new java.awt.Dimension(85, 40));
        buttonSendChat.setMinimumSize(new java.awt.Dimension(85, 40));
        buttonSendChat.setPreferredSize(new java.awt.Dimension(85, 40));
        buttonSendChat.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/send_button_default _ off_click.png"))); // NOI18N
        buttonSendChat.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/send_button_default _ off_hover.png"))); // NOI18N
        buttonSendChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendChatActionPerformed(evt);
            }
        });

        textChatInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textChatInputKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelChattLayout = new javax.swing.GroupLayout(panelChatt);
        panelChatt.setLayout(panelChattLayout);
        panelChattLayout.setHorizontalGroup(
            panelChattLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChattLayout.createSequentialGroup()
                .addComponent(textChatInput, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSendChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelChattLayout.setVerticalGroup(
            panelChattLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChattLayout.createSequentialGroup()
                .addComponent(textChatInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(buttonSendChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        buttonStream.setText("Start Streaming");
        buttonStream.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStreamActionPerformed(evt);
            }
        });

        buttonShowUsers.setText("Show Users");
        buttonShowUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowUsersActionPerformed(evt);
            }
        });

        buttonShowChat.setText("Show Chat");
        buttonShowChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowChatActionPerformed(evt);
            }
        });

        canvas.setMinimumSize(new java.awt.Dimension(590, 484));
        jSplitPane1.setLeftComponent(canvas);

        textChatOutput.setEditable(false);
        textChatOutput.setBackground(new java.awt.Color(102, 102, 102));
        textChatOutput.setColumns(20);
        textChatOutput.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        textChatOutput.setForeground(new java.awt.Color(255, 255, 255));
        textChatOutput.setRows(5);
        scrollPaneChatt.setViewportView(textChatOutput);

        jSplitPane1.setRightComponent(scrollPaneChatt);

        buttonShowPlaylist.setText("Show Playlist");
        buttonShowPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowPlaylistActionPerformed(evt);
            }
        });

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
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textFileLocation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonStream)
                                .addGap(9, 9, 9))
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonPause, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelChatt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonShowChat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonShowUsers)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonShowPlaylist)))))
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
                    .addComponent(buttonShowChat)
                    .addComponent(buttonShowUsers)
                    .addComponent(buttonShowPlaylist))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelChatt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textFileLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonStream))
                    .addComponent(buttonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonPause, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedMedia = fileChooser.getSelectedFile();
            playlist.addToPlaylist(selectedMedia.getName(), selectedMedia.getAbsolutePath(), "20min lol", "videofil");
            updateRightPanel(getPlaylist());
        }
    }//GEN-LAST:event_OpenActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_ExitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        serverNameGUI.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlayActionPerformed
        if (!playlist.isEmpty()) {
            mediaPlayer.playMedia(getRtspUrl());
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
        serverMediaPlayer.stop();
        mediaPlayer.stop();
    }//GEN-LAST:event_ButtonStopActionPerformed

    private void buttonStreamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStreamActionPerformed
        if (!playlist.isEmpty()) {
            startStreamingServer();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            mediaPlayer.playMedia(getRtspUrl());
        }
    }//GEN-LAST:event_buttonStreamActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (mediaPlayer.isPlaying()) {
            fullscreenplayer.fullscreen();
            mediaPlayer.setFullScreen(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void buttonSendChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendChatActionPerformed
        addToChat(textChatInput.getText());
        textChatInput.setText("");
    }//GEN-LAST:event_buttonSendChatActionPerformed

    private void textChatInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textChatInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addToChat(textChatInput.getText());
            textChatInput.setText("");
        }
    }//GEN-LAST:event_textChatInputKeyPressed

    private void buttonShowUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowUsersActionPerformed
        updateRightPanel(getUsers());
    }//GEN-LAST:event_buttonShowUsersActionPerformed

    private void buttonShowChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowChatActionPerformed
        jSplitPane1.setRightComponent(scrollPaneChatt);
    }//GEN-LAST:event_buttonShowChatActionPerformed

    private void buttonShowPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowPlaylistActionPerformed
        updateRightPanel(getPlaylist());
    }//GEN-LAST:event_buttonShowPlaylistActionPerformed

    public void connectToRtsp() {
        mediaPlayer.playMedia(getRtspUrl());
    }

    public void updateRightPanel(ArrayList<String> elements) {
        DefaultListModel model = (DefaultListModel) listInScrollpane.getModel();
        model.clear();

        for (String element : elements) {
            model.addElement(element);
        }

        jSplitPane1.setRightComponent(scrollListPanel);
    }

    public void addToChat(String text) {
        if (!text.isEmpty()) {
            textChatOutput.append("\r\n" + this.nick + ": " + text);
        }
    }

    public void startStreamingServer() {
        try {
            Thread streamingServer = new Thread() {
                public void run() {
                    try {
                        String media = playlist.getNowPlaying();
                        //No ip address here, only an @. 
                        final String options = formatRtspStream("@", 5555, "demo");
                        System.out.println("Streaming '" + media + "' to '" + options + "'");
                        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
                        serverMediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

                        serverMediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                            @Override
                            public void finished(MediaPlayer mediaPlayer) {
                                if (!playlist.isEmpty()) {
                                    streamNextMedia(mediaPlayer);
                                }
                            }

                            @Override
                            public void stopped(MediaPlayer mediaPlayer) {
                                if (!playlist.isEmpty()) {
                                    streamNextMedia(mediaPlayer);
                                }
                            }

                            private void streamNextMedia(MediaPlayer mediaPlayer) {
                                System.out.println("Playing next video after 2sec...");
                                playlist.removeFirstInQueue();
                                updateRightPanel(getPlaylist());
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                mediaPlayer.playMedia(playlist.getNowPlaying(),
                                        options,
                                        ":no-sout-rtp-sap",
                                        ":no-sout-standard-sap",
                                        ":sout-all",
                                        ":sout-keep"
                                );
                                pr.DeliverStreamToComunity(pr.getMyIp(), "demo");
                            }
                        });

                        serverMediaPlayer.playMedia(media,
                                options,
                                ":no-sout-rtp-sap",
                                ":no-sout-standard-sap",
                                ":sout-all",
                                ":sout-keep"
                        );
                        pr.DeliverStreamToComunity(pr.getMyIp(), "demo");
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
                new Client(new Peer()).setVisible(true);
            }
        });
    }

    // TODO un-hardcode
    private ArrayList<String> getUsers() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Sadok");
        list.add("Jehova");
        list.add("Judas");
        list.add("Gandalf");
        return list;
    }

    // TODO : un-hardcode
    private ArrayList<String> getPlaylist() {
        ArrayList<String> list = new ArrayList<String>();
        for (Pair entry : playlist.getMediaList()) {
            list.add("Owner: " + entry.key() + " || " + entry.value().getName());
        }
        return list;
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
    private javax.swing.JButton buttonSendChat;
    private javax.swing.JButton buttonShowChat;
    private javax.swing.JButton buttonShowPlaylist;
    private javax.swing.JButton buttonShowUsers;
    private javax.swing.JButton buttonStream;
    private java.awt.Canvas canvas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JList listInScrollpane;
    private javax.swing.JPanel panelChatt;
    private javax.swing.JScrollPane scrollListPanel;
    private javax.swing.JScrollPane scrollPaneChatt;
    private javax.swing.JTextField textChatInput;
    private javax.swing.JTextArea textChatOutput;
    private javax.swing.JTextField textFileLocation;
    // End of variables declaration//GEN-END:variables
}
