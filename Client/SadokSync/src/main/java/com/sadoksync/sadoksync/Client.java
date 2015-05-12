package com.sadoksync.sadoksync;

import com.sadoksync.sadoksync.ActionExitToLobby;
import com.sadoksync.sadoksync.EmptyCanvas;
import com.sadoksync.sadoksync.FileFilter;
import com.sadoksync.sadoksync.Message;
import com.sadoksync.sadoksync.Peer;
import com.sadoksync.sadoksync.PublicPlaylist;
import com.sadoksync.sadoksync.PublicPlaylist.Pair;
import com.sadoksync.sadoksync.StreamThreadManager;
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
                        if (fullscreenplayer.isActive()) {
                            fullscreenplayer.stopFullscreen();
                        }
                        if (playlist.isEmpty()) {
                            setLeftComponent(emptyCanvas);
                        }
                    }

                    @Override
                    public void stopped(MediaPlayer mediaPlayer
                    ) {
                        if (fullscreenplayer.isActive()) {
                            fullscreenplayer.stopFullscreen();
                        }
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
    void updateLabels() {
        labelIP.setText("My IP: " + pr.myIP);
        labelCom.setText("Connected to " + pr.com.getComunityName() + "@Registry IP: " + pr.com.getRegistryAddr());
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
        jPanel1 = new javax.swing.JPanel();
        chat = new javax.swing.JButton();
        users = new javax.swing.JButton();
        playList = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        canvas = new java.awt.Canvas();
        scrollPaneChatt = new javax.swing.JScrollPane();
        textChatOutput = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        textChatInput = new javax.swing.JTextField();
        send = new javax.swing.JButton();
        fullScreen = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        labelVolume = new javax.swing.JLabel();
        reconnect = new javax.swing.JButton();
        stop = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        labelIP = new javax.swing.JLabel();
        labelCom = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        open = new javax.swing.JMenuItem();
        addUrl = new javax.swing.JMenuItem();
        exitLobby = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        aboutUs = new javax.swing.JMenuItem();

        DefaultListModel listModel = new DefaultListModel();
        listInScrollpane.setModel(listModel);
        scrollListPanel.setViewportView(listInScrollpane);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(82, 68, 68));

        jPanel1.setBackground(new java.awt.Color(84, 84, 86));

        chat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chat_default_button_real.png"))); // NOI18N
        chat.setBorderPainted(false);
        chat.setContentAreaFilled(false);
        chat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chat.setPreferredSize(new java.awt.Dimension(85, 25));
        chat.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/chat_pressed_real.png"))); // NOI18N
        chat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatActionPerformed(evt);
            }
        });

        users.setIcon(new javax.swing.ImageIcon(getClass().getResource("/users_default_real.png"))); // NOI18N
        users.setContentAreaFilled(false);
        users.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        users.setMaximumSize(new java.awt.Dimension(85, 25));
        users.setMinimumSize(new java.awt.Dimension(85, 25));
        users.setPreferredSize(new java.awt.Dimension(85, 25));
        users.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/users_pressed_real.png"))); // NOI18N
        users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersActionPerformed(evt);
            }
        });

        playList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/playlist_default_real.png"))); // NOI18N
        playList.setContentAreaFilled(false);
        playList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playList.setMaximumSize(new java.awt.Dimension(85, 25));
        playList.setMinimumSize(new java.awt.Dimension(85, 25));
        playList.setPreferredSize(new java.awt.Dimension(85, 25));
        playList.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/playlist_pressed_real.png"))); // NOI18N
        playList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playListActionPerformed(evt);
            }
        });

        canvas.setBackground(new java.awt.Color(102, 102, 102));
        canvas.setMinimumSize(new java.awt.Dimension(590, 484));
        jSplitPane1.setLeftComponent(canvas);

        scrollPaneChatt.setBackground(new java.awt.Color(102, 102, 102));

        textChatOutput.setEditable(false);
        textChatOutput.setBackground(new java.awt.Color(102, 102, 102));
        textChatOutput.setColumns(20);
        textChatOutput.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        textChatOutput.setForeground(new java.awt.Color(255, 255, 255));
        textChatOutput.setRows(5);
        scrollPaneChatt.setViewportView(textChatOutput);

        jSplitPane1.setRightComponent(scrollPaneChatt);

        textChatInput.setBackground(new java.awt.Color(102, 102, 102));
        textChatInput.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        textChatInput.setForeground(new java.awt.Color(255, 255, 255));
        textChatInput.setCaretColor(new java.awt.Color(255, 255, 255));
        textChatInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textChatInputKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textChatInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textChatInput, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        send.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send_default_real.png"))); // NOI18N
        send.setContentAreaFilled(false);
        send.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        send.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/send_pressed_real.png"))); // NOI18N
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

        fullScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fullScreen_default_real.png"))); // NOI18N
        fullScreen.setContentAreaFilled(false);
        fullScreen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fullScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullScreenActionPerformed(evt);
            }
        });

        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        labelVolume.setText("100 %");

        reconnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Reconnect_default_real.png"))); // NOI18N
        reconnect.setContentAreaFilled(false);
        reconnect.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reconnect.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Reconnet_pressed_real.png"))); // NOI18N
        reconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reconnectActionPerformed(evt);
            }
        });

        stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stop_default_real.png"))); // NOI18N
        stop.setContentAreaFilled(false);
        stop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stop.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/stop_pressed_real.png"))); // NOI18N
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SADOK_LOGO_TEST.png"))); // NOI18N

        labelIP.setForeground(new java.awt.Color(255, 255, 255));
        labelIP.setText("My IP:");

        labelCom.setForeground(new java.awt.Color(255, 255, 255));
        labelCom.setText("Reconnect to X in X");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(stop, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(reconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                        .addComponent(labelVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fullScreen, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelIP)
                            .addComponent(labelCom))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chat, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(users, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playList, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(send, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1036, Short.MAX_VALUE)
                    .addGap(12, 12, 12)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(users, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(playList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelIP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelCom)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 472, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(labelVolume)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reconnect)
                        .addComponent(stop)
                        .addComponent(fullScreen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(11, 11, 11))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 444, Short.MAX_VALUE)
                    .addGap(67, 67, 67)))
        );

        jMenu1.setText("File");

        open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Yellow_folder_icon_open.png"))); // NOI18N
        open.setText("Open");
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        jMenu1.add(open);

        addUrl.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        addUrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/link.png"))); // NOI18N
        addUrl.setText("Add url");
        addUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUrlActionPerformed(evt);
            }
        });
        jMenu1.add(addUrl);

        exitLobby.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        exitLobby.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GeoGebra_icon_exit.png"))); // NOI18N
        exitLobby.setText("Exit lobby");
        exitLobby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitLobbyActionPerformed(evt);
            }
        });
        jMenu1.add(exitLobby);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        aboutUs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/about16.png"))); // NOI18N
        aboutUs.setText("About us");
        aboutUs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutUsActionPerformed(evt);
            }
        });
        jMenu2.add(aboutUs);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUrlActionPerformed
      String url = JOptionPane.showInputDialog(this, "Enter a Youtube URL");
        //String url = "https://www.youtube.com/watch?v=wZZ7oFKsKzY";
       if (url != null) {  
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
    }//GEN-LAST:event_addUrlActionPerformed
    }
    private void exitLobbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitLobbyActionPerformed
         //Move to ActionExitToLobby
        // Remove client and its music from playlist.
        ActionExitToLobby aetl = new ActionExitToLobby(pr, stm, mediaPlayer);
        new Thread(aetl).start();


    }//GEN-LAST:event_exitLobbyActionPerformed

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedMedia = fileChooser.getSelectedFile();
            playlist.addToPlaylist(pr.getNick(), selectedMedia);
            setMode("playlist");
        }
    }//GEN-LAST:event_openActionPerformed

    private void chatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatActionPerformed
          if (!rightPanelMode.equals("chat")) {
            setMode("chat");
        }
    }//GEN-LAST:event_chatActionPerformed

    private void usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersActionPerformed
          if (!rightPanelMode.equals("users")) {
            setMode("users");
        }
    }//GEN-LAST:event_usersActionPerformed

    private void playListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playListActionPerformed
          if (!rightPanelMode.equals("playlist")) {
            setMode("playlist");
        }
    }//GEN-LAST:event_playListActionPerformed

    private void textChatInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textChatInputKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addToChat(textChatInput.getText());
            textChatInput.setText("");
        }
    }//GEN-LAST:event_textChatInputKeyPressed

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
    }//GEN-LAST:event_jSlider1StateChanged
    }
    private void reconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reconnectActionPerformed
           stopped = false;
           connectToRtsp();
    }//GEN-LAST:event_reconnectActionPerformed

    private void stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopActionPerformed
       if(!mediaReleased){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (pr.isHost()) {
            //mediaServer.stop();
            stm.stop();
        } else {
            stopped = true;
        }
    }//GEN-LAST:event_stopActionPerformed
    }
    private void fullScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullScreenActionPerformed
      if (mediaPlayer.isPlaying()) {
            if (!visualizeMode) {
                fullscreenplayer.fullscreen(mediaPlayerFactory, mediaPlayer);
                mediaPlayer.setFullScreen(true);
            } else {
                System.out.println("Visualizer in fullscreen not supported :((");
            }
        }
    }//GEN-LAST:event_fullScreenActionPerformed

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
         addToChat(textChatInput.getText());
        textChatInput.setText("");
    }//GEN-LAST:event_sendActionPerformed

    private void aboutUsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutUsActionPerformed
      webSiteOpener run = new webSiteOpener();
      run.openMyWebSite("http://sadoksync.site90.net/");
    }//GEN-LAST:event_aboutUsActionPerformed

    public void connectToRtsp() {
        if (!stopped) {
            playMedia(getRtspUrl());
        }
    }
    
     public StreamThreadManager getStm(){
        return stm;
    }
    
     public EmbeddedMediaPlayer getMedaPlayer(){
        return mediaPlayer;
    }

    public void playMedia(String url) {
        mediaReleased = true;
         if (fullscreenplayer.isActive()) {
            fullscreenplayer.stopFullscreen();
        }
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

        stm.startStream(playlist,this );
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
  // public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       // try {
         //   for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
           //     if ("Nimbus".equals(info.getName())) {
             //       javax.swing.UIManager.setLookAndFeel(info.getClassName());
               //     break;

                //}
         //   }
        //} catch (ClassNotFoundException ex) {
          //  java.util.logging.Logger.getLogger(recreatedClient.class
            ///        .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        //} catch (InstantiationException ex) {
          //  java.util.logging.Logger.getLogger(recreatedClient.class
            //        .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        //} catch (IllegalAccessException ex) {
          //  java.util.logging.Logger.getLogger(recreatedClient.class
          //          .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        //} catch (javax.swing.UnsupportedLookAndFeelException ex) {
           // java.util.logging.Logger.getLogger(recreatedClient.class
             //       .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        //}
        //</editor-fold>

        //VLC now included in project instead
        //NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");

        /* Create and display the form */
    //    java.awt.EventQueue.invokeLater(new Runnable() {
      //      @Override
        //    public void run() {
          //      new recreatedClient(new Peer()).setVisible(true);
           // }
       // });
    //}

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
        private boolean active;

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
            active = false;
        }
         private boolean isActive() {
            return active;
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
             active = true;

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
            active = false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutUs;
    private javax.swing.JMenuItem addUrl;
    private java.awt.Canvas canvas;
    private javax.swing.JButton chat;
    private javax.swing.JMenuItem exitLobby;
    private javax.swing.JButton fullScreen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel labelCom;
    private javax.swing.JLabel labelIP;
    private javax.swing.JLabel labelVolume;
    private javax.swing.JList listInScrollpane;
    private javax.swing.JMenuItem open;
    private javax.swing.JButton playList;
    private javax.swing.JButton reconnect;
    private javax.swing.JScrollPane scrollListPanel;
    private javax.swing.JScrollPane scrollPaneChatt;
    private javax.swing.JButton send;
    private javax.swing.JButton stop;
    private javax.swing.JTextField textChatInput;
    private javax.swing.JTextArea textChatOutput;
    private javax.swing.JButton users;
    // End of variables declaration//GEN-END:variables
}
