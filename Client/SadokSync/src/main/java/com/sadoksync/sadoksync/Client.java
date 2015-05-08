package com.sadoksync.sadoksync;

import com.sadoksync.sadoksync.PublicPlaylist.Pair;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.directaudio.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;

/**
 *
 * @author Arouz
 */
public class Client extends javax.swing.JFrame {

    private StreamThreadManager stm;
    private String mediaType;

    // Create a media player factory
    private MediaPlayerFactory mediaPlayerFactory;

    // Create a new media player instance for the run-time platform
    EmbeddedMediaPlayer mediaPlayer;

    // Create video surface
    public CanvasVideoSurface videoSurface;

    // Create the mediaServer
    //private MediaServer mediaServer;
    // Create fullscreen player
    private final FullScreenPlayer fullscreenplayer;

    // Create the public playlist
    private PublicPlaylist playlist;

    // Create windows-look file chooser
    private JFileChooser fileChooser;

    // Right panel mode
    public String rightPanelMode;

    // Rtsp variables
    private String server;
    private String port;
    private String rtspPath;

    // Socket variables
    private boolean isHost;
    Peer pr;

    // Mode
    private boolean visualizeMode;

    // File filter
    public FileFilter filefilter;

    // "pause/stop" stream flag for clients
    private boolean stopped;

    // init empty canvas
    final private EmptyCanvas emptyCanvas;
    private boolean mediaReleased;

    /**
     * Creates new form Client
     *
     * @param pr Peer
     */
    public Client(Peer pr) {
        stm = new StreamThreadManager(pr.getDebugSys());

        // Init style and layout
        initStyle();
        // Now create rest of components with saved default layout
        initComponents();

        // Drag and drop init
        initDragnDrop();

        // Peer init
        this.pr = pr;

        //VLCLibrary init
        NativeLibrary.addSearchPath("libvlc", "VLC");

        //File chooser settings init
        fileChooser.setFileFilter(
                new FileFilter());
        fileChooser.setAcceptAllFileFilterUsed(
                false);

        //Fullscreenplayer init
        fullscreenplayer = new FullScreenPlayer();

        //Media player init
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(fullscreenplayer.frame));

        //Set mediaplayer to surface
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas);

        mediaPlayer.setVideoSurface(videoSurface);
        visualizeMode = false;

        //Init empty canvas
        emptyCanvas = new EmptyCanvas();

        this.mediaPlayerInit(mediaPlayer);

        // Split panel inits
        jSplitPane1.setDividerLocation(
                0.7);
        rightPanelMode = "chat";

        // Public playlist init
        playlist = new PublicPlaylist(pr);

        // FileFilter init
        filefilter = new FileFilter();

        // Set default left canas to the empty canvas
        setLeftComponent(emptyCanvas);

        stopped = false;
        mediaReleased = false;
        /*
         this.isHost = pr.isHost();
         if (!isHost) {
         buttonStream.setEnabled(false);
         }
         */
    }

    private void mediaPlayerInit(EmbeddedMediaPlayer mediaPlayer) {
        mediaPlayer.addMediaPlayerEventListener(
                new MediaPlayerEventAdapter() {
                    @Override
                    public void finished(MediaPlayer mediaPlayer
                    ) {
                        if (playlist.isEmpty()) {
                            setLeftComponent(emptyCanvas);
                        }
                    }

                    @Override
                    public void stopped(MediaPlayer mediaPlayer
                    ) {
                        if (playlist.isEmpty()) {
                            setLeftComponent(emptyCanvas);
                        }
                    }
                }
        );
    }

    public void persistClient(MediaPlayer mediaPlayer) {
        System.out.println("Playing next video after 5sec...(client)");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        playMedia(getRtspUrl());
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

    public void setMode(String mode) {
        switch (mode) {
            case "chat":
                this.rightPanelMode = mode;
                int temp = jSplitPane1.getDividerLocation();
                jSplitPane1.setRightComponent(scrollPaneChatt);
                jSplitPane1.setDividerLocation(temp);
                break;
            case "users":
                this.rightPanelMode = mode;
                updateRightPanel(getUsers());
                break;
            case "playlist":
                this.rightPanelMode = mode;
                updateRightPanel(getPlaylist());
                break;
        }
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
        textChatInput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        buttonPlay = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        ButtonStop = new javax.swing.JButton();
        buttonShowUsers = new javax.swing.JButton();
        buttonShowChat = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        canvas = new java.awt.Canvas();
        scrollPaneChatt = new javax.swing.JScrollPane();
        textChatOutput = new javax.swing.JTextArea();
        buttonShowPlaylist = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        labelVolume = new javax.swing.JLabel();
        buttonSendChat = new javax.swing.JButton();
        labelIP = new javax.swing.JLabel();
        labelCom = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        Open = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
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

        panelChatt.setBackground(new java.awt.Color(102, 102, 102));

        textChatInput.setBackground(new java.awt.Color(102, 102, 102));
        textChatInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textChatInputKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelChattLayout = new javax.swing.GroupLayout(panelChatt);
        panelChatt.setLayout(panelChattLayout);
        panelChattLayout.setHorizontalGroup(
            panelChattLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textChatInput)
        );
        panelChattLayout.setVerticalGroup(
            panelChattLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textChatInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("S A D O K S Y N C !");

        buttonPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Reconnect_button_default.png"))); // NOI18N
        buttonPlay.setBorderPainted(false);
        buttonPlay.setContentAreaFilled(false);
        buttonPlay.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        buttonPlay.setMaximumSize(new java.awt.Dimension(85, 40));
        buttonPlay.setMinimumSize(new java.awt.Dimension(85, 40));
        buttonPlay.setPreferredSize(new java.awt.Dimension(85, 40));
        buttonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlayActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fullscreen_Button_default.png"))); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton3.setMaximumSize(new java.awt.Dimension(85, 40));
        jButton3.setMinimumSize(new java.awt.Dimension(85, 40));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        ButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stop_Button_default.png"))); // NOI18N
        ButtonStop.setBorderPainted(false);
        ButtonStop.setContentAreaFilled(false);
        ButtonStop.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ButtonStop.setMaximumSize(new java.awt.Dimension(85, 40));
        ButtonStop.setMinimumSize(new java.awt.Dimension(85, 40));
        ButtonStop.setPreferredSize(new java.awt.Dimension(85, 40));
        ButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonStopActionPerformed(evt);
            }
        });

        buttonShowUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Users_Button_default.png"))); // NOI18N
        buttonShowUsers.setBorderPainted(false);
        buttonShowUsers.setContentAreaFilled(false);
        buttonShowUsers.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        buttonShowUsers.setMaximumSize(new java.awt.Dimension(85, 40));
        buttonShowUsers.setMinimumSize(new java.awt.Dimension(85, 40));
        buttonShowUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowUsersActionPerformed(evt);
            }
        });

        buttonShowChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat_button_default.png"))); // NOI18N
        buttonShowChat.setBorderPainted(false);
        buttonShowChat.setContentAreaFilled(false);
        buttonShowChat.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        buttonShowChat.setMaximumSize(new java.awt.Dimension(85, 40));
        buttonShowChat.setMinimumSize(new java.awt.Dimension(85, 40));
        buttonShowChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowChatActionPerformed(evt);
            }
        });

        canvas.setMinimumSize(new java.awt.Dimension(590, 484));
        jSplitPane1.setLeftComponent(canvas);

        scrollPaneChatt.setBackground(new java.awt.Color(102, 102, 102));

        textChatOutput.setEditable(false);
        textChatOutput.setBackground(new java.awt.Color(102, 102, 102));
        textChatOutput.setColumns(20);
        textChatOutput.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        textChatOutput.setForeground(new java.awt.Color(255, 255, 255));
        textChatOutput.setRows(5);
        scrollPaneChatt.setViewportView(textChatOutput);

        jSplitPane1.setRightComponent(scrollPaneChatt);

        buttonShowPlaylist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/playlist_Button_default.png"))); // NOI18N
        buttonShowPlaylist.setBorderPainted(false);
        buttonShowPlaylist.setContentAreaFilled(false);
        buttonShowPlaylist.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        buttonShowPlaylist.setMaximumSize(new java.awt.Dimension(85, 40));
        buttonShowPlaylist.setMinimumSize(new java.awt.Dimension(85, 40));
        buttonShowPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowPlaylistActionPerformed(evt);
            }
        });

        jSlider1.setPaintLabels(true);
        jSlider1.setValue(100);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        labelVolume.setText("100 %");

        buttonSendChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send_button_default _ off.png"))); // NOI18N
        buttonSendChat.setBorderPainted(false);
        buttonSendChat.setContentAreaFilled(false);
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

        labelIP.setText("My IP: ");

        labelCom.setText("Connected to X in X");

        jMenuBar1.setBackground(new java.awt.Color(102, 102, 102));

        jMenu1.setText("File");

        Open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Yellow_folder_icon_open.png"))); // NOI18N
        Open.setText("Open");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        jMenu1.add(Open);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/link.png"))); // NOI18N
        jMenuItem3.setText("Add url");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GeoGebra_icon_exit.png"))); // NOI18N
        jMenuItem4.setText("Exit to lobby");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
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
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1038, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ButtonStop, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(labelVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelIP)
                                    .addComponent(labelCom))))
                        .addGap(0, 130, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonShowChat, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonShowUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonShowPlaylist, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelChatt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonSendChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonShowPlaylist, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonShowChat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonShowUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(labelIP)
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(labelCom))
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(labelVolume))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buttonSendChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(panelChatt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonPlay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ButtonStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedMedia = fileChooser.getSelectedFile();
            playlist.addToPlaylist(pr.getNick(), selectedMedia);
            setMode("playlist");
        }
    }//GEN-LAST:event_OpenActionPerformed

    private void buttonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPlayActionPerformed
        stopped = false;
        connectToRtsp();
    }//GEN-LAST:event_buttonPlayActionPerformed

    private void ButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonStopActionPerformed
        if (!mediaReleased) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            if (pr.isHost()) {
                //mediaServer.stop();
                stm.stop();
            } else {
                stopped = true;
            }
        }
    }//GEN-LAST:event_ButtonStopActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (mediaPlayer.isPlaying()) {
            if (!visualizeMode) {
                fullscreenplayer.fullscreen(mediaPlayerFactory, mediaPlayer);
                mediaPlayer.setFullScreen(true);
            } else {
                System.out.println("Visualizer in fullscreen not supported :((");
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void buttonShowUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowUsersActionPerformed
        if (!rightPanelMode.equals("users")) {
            setMode("users");
        }
    }//GEN-LAST:event_buttonShowUsersActionPerformed

    private void buttonShowChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowChatActionPerformed
        if (!rightPanelMode.equals("chat")) {
            setMode("chat");
        }
    }//GEN-LAST:event_buttonShowChatActionPerformed

    private void buttonShowPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowPlaylistActionPerformed
        if (!rightPanelMode.equals("playlist")) {
            setMode("playlist");
        }
    }//GEN-LAST:event_buttonShowPlaylistActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        String url = JOptionPane.showInputDialog(this, "Enter a Youtube URL");
        //String url = "https://www.youtube.com/watch?v=wZZ7oFKsKzY";
        if (!url.contains("v=")) //there's no video id
        {
            JOptionPane.showMessageDialog(this, "No Youtube-ID in URL");
        } else {
            String id = url.split("v=")[1]; // we want everything after 'v='
            int end_of_id = id.indexOf("&"); // if there are other parameters in the url, get only the id's value
            if (end_of_id != -1) {
                id = url.substring(0, end_of_id);
            }
            if (!id.isEmpty()) {
                playlist.addToPlaylist(pr.getNick(), id, "youtube");
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        //Move to ActionExitToLobby
        // Remove client and its music from playlist.
        ActionExitToLobby aetl = new ActionExitToLobby(pr, stm, mediaPlayer);
        new Thread(aetl).start();


    }//GEN-LAST:event_jMenuItem4ActionPerformed
    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        Object source = evt.getSource();
        if (source instanceof JSlider) {
            if (!mediaReleased) {
                System.out.println(mediaPlayer.isPlayable());
                JSlider theJSlider = (JSlider) source;
                System.out.println("Slider changed: " + theJSlider.getValue());
                mediaPlayer.setVolume(theJSlider.getValue());
                labelVolume.setText(theJSlider.getValue() + " %");
            }
        }
    }//GEN-LAST:event_jSlider1StateChanged

    private void textChatInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textChatInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addToChat(textChatInput.getText());
            textChatInput.setText("");
        }
    }//GEN-LAST:event_textChatInputKeyPressed

    private void buttonSendChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendChatActionPerformed
        addToChat(textChatInput.getText());
        textChatInput.setText("");
    }//GEN-LAST:event_buttonSendChatActionPerformed

    public void connectToRtsp() {
        if (!stopped) {
            playMedia(getRtspUrl());
        }
    }

    public void playMedia(String url) {
        mediaReleased = true;

        mediaPlayer.release();

        mediaPlayerFactory.release();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        //String[] s = playlist.getNowPlaying().split("\\.");
        // media ends with mp3? Then we want a visualizer
        //if (s[s.length - 1].endsWith("mp3")) {
        System.out.println("[Client.PlayMedia] mediaType: " + mediaType + ", url: " + url);
        if (mediaType.equals("visualize")) {
            // Check if visualizemode already set, else we need to set it to visualizemode
            // If not, its already in visualizemode and we can play media without any changes

            System.out.println("1");
            // To set it to visualize mode we need to:
            // Recreate the mediaPlayerFactory with visualizer options
            mediaPlayerFactory = new MediaPlayerFactory("--audio-visual=visual", "--effect-list=spectrum");
            System.out.println("2");
            mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(fullscreenplayer.frame));
            System.out.println("3");
            //Set visualizer mediaplayer to surface
            videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
            System.out.println("4");
            mediaPlayer.setVideoSurface(videoSurface);
            System.out.println("5");
            this.mediaPlayerInit(mediaPlayer);

            // Own visualizer stuff. dont remove pls
            // I think this gives us a audiostream to work with
            /*MediaPlayerFactory factory = new MediaPlayerFactory();
             DirectAudioPlayer audioPlayer = factory.newDirectAudioPlayer("S16N", 44100, 2, new TestAudioCallbackAdapter());
             audioPlayer.playMedia(url);*/
        } else {
            // Check if in visualizemode, if it is we need to recreate the mediaplayer

            System.out.println("1");
            // Recreate the mediaplayerfactory without visualizer options
            //"--realrtsp-caching=1200", manual cache size. 
            mediaPlayerFactory = new MediaPlayerFactory();
            mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(fullscreenplayer.frame));

            //Set mediaplayer without visualize options to surface
            videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
            mediaPlayer.setVideoSurface(videoSurface);
            this.mediaPlayerInit(mediaPlayer);

        }
        // lastly, play the media in the mediaplayer with the apropriate options
        setLeftComponent(canvas);
        mediaReleased = false;
        mediaPlayer.setVolume(jSlider1.getValue());
        mediaPlayer.playMedia(url);
    }

    public void setMediaType(String text) {
        this.mediaType = text;

    }

    public void clearPlaylist() {
        playlist.clear();
    }

    void updateLabels() {
        labelIP.setText("My IP: " + pr.myIP);
        labelCom.setText("Connected to " + pr.com.getComunityName() + "@Registry IP: " + pr.com.getRegistryAddr());
    }

    private class TestAudioCallbackAdapter extends DefaultAudioCallbackAdapter {

        /**
         * Output stream.
         */
        private final BufferedOutputStream out;

        /**
         * Create an audio callback.
         */
        public TestAudioCallbackAdapter() {
            super(4); // 4 is the block size for the audio samples
            out = new BufferedOutputStream(System.out);
        }

        @Override
        protected void onPlay(DirectAudioPlayer mediaPlayer, byte[] data, int sampleCount, long pts) {
            try {
                out.write(data);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateRightPanel(ArrayList<String> elements) {
        DefaultListModel model = (DefaultListModel) listInScrollpane.getModel();
        model.clear();

        for (String element : elements) {
            model.addElement(element);
        }

        int temp = jSplitPane1.getDividerLocation();
        scrollListPanel.setSize(jSplitPane1.getRightComponent().getSize());
        jSplitPane1.setRightComponent(scrollListPanel);
        jSplitPane1.setDividerLocation(temp);
    }

    public void addToChat(String text) {
        if (!text.isEmpty()) {
            Message msg = new Message();
            msg.setipAddr(pr.getMyIp());
            // TODO - validate IP with nick for security
            msg.setType("chat message");
            msg.setText(pr.getNick() + ": " + text);
            pr.sendMsgToComunity(msg);
            addToChatOutput(msg.getText());
        }
    }

    public void addToChatOutput(String text) {
        textChatOutput.append("\r\n" + text);
    }

    public void startStreamingServer() {

        stm.startStream(playlist, this);
        //mediaServer = new MediaServer(playlist, this);
        //mediaServer.startStreamingServer();
    }

    public void cleanStartOfPlaylist() {
        while (!pr.getMyIp().equals(pr.com.getPeerIP(playlist.getFirstInListOwner()))) {
            playlist.removeFirstInQueue();
        }
    }

    public boolean isPlaylistEmpty() {
        return playlist.isEmpty();
    }

    public void startStream() {
        if (!playlist.isEmpty()) {
            String ip = pr.com.getPeerIP(playlist.getFirstInListOwner());
            if (pr.getMyIp().equals(ip)) {
                startStreamingServer();

                /*
                 playMedia(getRtspUrl());
                 pr.DeliverStreamToComunity(pr.getMyIp(), "demo");
                 */
            } else {
                pr.Ping(ip, "Move Host");
            }
        } else {
            System.out.println("[Client.startStream] No more media in list!");
            setLeftComponent(emptyCanvas);
        }
    }

    void setLeftComponent(Canvas canvas) {
        int temp = jSplitPane1.getDividerLocation();
        jSplitPane1.setLeftComponent(canvas);
        jSplitPane1.setDividerLocation(temp);
    }

    private void setLeftComponent(EmptyCanvas canvas) {
        int temp = jSplitPane1.getDividerLocation();
        jSplitPane1.setLeftComponent(canvas);
        jSplitPane1.setDividerLocation(temp);
    }

    /**
     * @param args the command line arguments
     */
    /*
     public static void main(String args[]) {
     // Set the Nimbus look and feel
     //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
     // If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     // For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     //
     try {
     for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
     if ("Nimbus".equals(info.getName())) {
     javax.swing.UIManager.setLookAndFeel(info.getClassName());
     break;

     }
     }
     } catch (ClassNotFoundException ex) {
     java.util.logging.Logger.getLogger(Client.class
     .getName()).log(java.util.logging.Level.SEVERE, null, ex);
     } catch (InstantiationException ex) {
     java.util.logging.Logger.getLogger(Client.class
     .getName()).log(java.util.logging.Level.SEVERE, null, ex);
     } catch (IllegalAccessException ex) {
     java.util.logging.Logger.getLogger(Client.class
     .getName()).log(java.util.logging.Level.SEVERE, null, ex);
     } catch (javax.swing.UnsupportedLookAndFeelException ex) {
     java.util.logging.Logger.getLogger(Client.class
     .getName()).log(java.util.logging.Level.SEVERE, null, ex);
     }
     //</editor-fold>

     //VLC now included in project instead
     //NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");

     // Create and display the form 
     java.awt.EventQueue.invokeLater(new Runnable() {
     @Override
     public void run() {
     new Client(new Peer()).setVisible(true);
     }
     });
     }
     */
    private ArrayList<String> getUsers() {
        ArrayList<String> list = new ArrayList<String>();
        Map map = pr.com.getComunityPeers();

        for (Object entry : map.keySet()) {
            String key = entry.toString();
            list.add(key);
        }

        return list;
    }

    public ArrayList<String> getPlaylist() {
        ArrayList<String> list = new ArrayList<String>();
        playlist.getLock().lock();
        try {
            for (Pair entry : playlist.getMediaList()) {
                long millis = entry.value().getLength();
                String length = String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );
                list.add("[" + length + "] || " + entry.value().getName() + " || Owned by " + entry.key());
            }
        } finally {
            playlist.getLock().unlock();
        }

        return list;
    }

    public void addtoPlaylist(PublicPlaylist.Pair pair) {
        playlist.addToPlaylist(pair);
        updateRightPanel(getPlaylist());
    }

    public PublicPlaylist getPublicPlaylist() {
        return playlist;
    }

    private void initStyle() {
        // Nimbus L&F
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // Save java Look&Feel (L&F)
        LookAndFeel originalLaf = UIManager.getLookAndFeel();
        try {
            // Switch to Windows L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //Create the Windows L&F file chooser
        fileChooser = new JFileChooser();

        try {
            //Flick the L&F back to the default
            UIManager.setLookAndFeel(originalLaf);

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initDragnDrop() {
        listInScrollpane.setDragEnabled(true);

        TransferHandler handler = new TransferHandler() {

            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                // we only import FileList
                if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                if (!info.isDrop()) {
                    return false;
                }

                // If mode isn't "playlist", fail
                if (!rightPanelMode.equals("playlist")) {
                    return false;
                }

                // Check for FileList flavor
                if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    displayDropLocation("List doesn't accept a drop of this type.");
                    return false;
                }

                // Get the fileList that is being dropped.
                Transferable t = info.getTransferable();
                List<File> data;
                try {
                    data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                } catch (Exception e) {
                    return false;
                }
                for (File file : data) {
                    if (filefilter.accept(file)) {
                        playlist.addToPlaylist(pr.getNick(), file);
                    } else {
                        displayDropLocation("Does only accept media files.");
                    }
                }
                updateRightPanel(getPlaylist());
                return true;
            }

            private void displayDropLocation(String string) {
                System.out.println(string);
            }
        };
        listInScrollpane.setTransferHandler(handler);
    }

    void setPlayList(PublicPlaylist playlist) {
        this.playlist = playlist;
        setMode("playlist");

    }

    class FullScreenPlayer {

        public Canvas canvas;
        public JFrame frame;
        public CanvasVideoSurface videoSurface;
        public EmbeddedMediaPlayer fullscreenMediaPlayer;

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

        private void fullscreen(MediaPlayerFactory fullscreenMediaPlayerFactory, EmbeddedMediaPlayer mediaPlayer) {
            this.videoSurface = fullscreenMediaPlayerFactory.newVideoSurface(this.canvas);
            this.fullscreenMediaPlayer = mediaPlayer;
            frame.setVisible(true);
            canvas.setVisible(true);

            // Workaround, disable videotrack while swapping surface
            int vid = this.fullscreenMediaPlayer.getVideoTrack();
            this.fullscreenMediaPlayer.setVideoTrack(-1);

            this.fullscreenMediaPlayer.setVideoSurface(this.videoSurface);
            this.fullscreenMediaPlayer.attachVideoSurface();

            // Put videotrack back
            this.fullscreenMediaPlayer.setVideoTrack(vid);

        }

        private void stopFullscreen() {
            // Workaround, disable videotrack while swapping surface
            int vid = fullscreenMediaPlayer.getVideoTrack();
            fullscreenMediaPlayer.setVideoTrack(-1);

            fullscreenMediaPlayer.setVideoSurface(Client.this.videoSurface);
            fullscreenMediaPlayer.attachVideoSurface();

            // Put videotrack back
            fullscreenMediaPlayer.setVideoTrack(vid);

            frame.setVisible(false);
            canvas.setVisible(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonStop;
    private javax.swing.JMenuItem Open;
    private javax.swing.JButton buttonPlay;
    private javax.swing.JButton buttonSendChat;
    private javax.swing.JButton buttonShowChat;
    private javax.swing.JButton buttonShowPlaylist;
    private javax.swing.JButton buttonShowUsers;
    private java.awt.Canvas canvas;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel labelCom;
    private javax.swing.JLabel labelIP;
    private javax.swing.JLabel labelVolume;
    private javax.swing.JList listInScrollpane;
    private javax.swing.JPanel panelChatt;
    private javax.swing.JScrollPane scrollListPanel;
    private javax.swing.JScrollPane scrollPaneChatt;
    private javax.swing.JTextField textChatInput;
    private javax.swing.JTextArea textChatOutput;
    // End of variables declaration//GEN-END:variables
}
