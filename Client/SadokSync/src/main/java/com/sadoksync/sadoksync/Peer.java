/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Pontus
 */
public class Peer {

    DebugSys dbs;

    ExecutorService executor;
    PeerServerThread pst;

    String nick;
    String myVlcPath;
    String myIP;

    Comunity com;
    boolean isConnected;

    //ClientInterface cri;
    ServiceRegistry sr;
    Lobby lb;
    Client cli;
    //Properties prop;

    //SynchReg synchMap;
    //Key is UUID, ComunityRegistration is a comunity registration
    Map<String, ComunityRegistration> cMap;

    public Peer(DebugSys dbs) {
        this.dbs = dbs;
        executor = Executors.newFixedThreadPool(50);
        this.cMap = Collections.synchronizedMap(new HashMap<String, ComunityRegistration>());
    }

    //del?
    void setComunityTopic(String topic) {
        //System.out.println("Peer: setComunityTopic:" + topic);
        dbs.println("Peer: setComunityTopic:" + topic);

        com.setTopic(topic);
    }

    //del?
    void createComunity(String cname, String topic) {
        String uniqueID = UUID.randomUUID().toString();
        com.create(cname, myIP, topic, nick, uniqueID);

        cMap.put(uniqueID, new ComunityRegistration(cname, myIP, topic, uniqueID));

        this.joinComunity(uniqueID);
    }

    void registerComunity(String rhost, int port) {
        com.setRegistryAddr(rhost);
        Message msg = new Message(com);
        this.sendMsg(rhost, port, msg);
    }

    void findAllComunity(String rhost, int port) {
        com.setRegistryAddr(rhost);

        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Find All");
        msg.setName(this.getNick());
        this.sendMsg(rhost, port, msg);
    }
    /*
     void joinComunity(String cname, String addr, int port) {
     Message msg = new Message();
     msg.setipAddr(this.getMyIp());
     msg.setType("Join Comunity");
     msg.setName(this.getNick());
     msg.setText(cname);      
     }
     */

    void joinComunity(String uuid) {
        ComunityRegistration cr = cMap.get(uuid);

        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Join Comunity");
        msg.setName(this.getNick());
        //msg.setText(cname);

        this.sendMsg(cr.getHost(), 40, msg);
        this.openClient();

    }

    void directConnect(String ip) {

        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Join Comunity");
        msg.setName(this.getNick());
        //msg.setText(cname);

        this.sendMsg(ip, 40, msg);
        this.openClient();
    }
    /*
     void setComunityHost(String host) {
     System.out.println("Peer: setComunityHost");

     this.openClient();
     com.setHost(host);

     this.joinComunity(com.getComunityName(), host, 40);
     }
     */

    void setLobby(Lobby lb) {
        this.lb = lb;
    }
    /*
     void setProp(Properties prop) {
     this.prop = prop;
     }
     */

    void setClient(Client cli) {
        this.cli = cli;
    }

    void setNick(String me) {
        this.nick = me;
        //com.setNick(this.nick);
    }

    String getNick() {
        return nick;
    }

    void setMyIP(String ipAddr) {
        this.myIP = ipAddr;
    }

    String getMyIp() {
        return this.myIP;
    }

    void setMyVlcPath(String vlcpath) {
        this.myVlcPath = vlcpath;
    }

    String getMyVlc() {
        return myVlcPath;
    }

    void openLobby() {
        final Lobby flb = lb;
        //final Properties fprop = prop;
        final Client fcli = cli;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(true);
                fcli.setVisible(false);
                //fprop.setVisible(false);
            }
        });
    }

    void openProperties() {
        final Lobby flb = lb;
        //final Properties fprop = prop;
        final Client fcli = cli;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(false);
                fcli.setVisible(false);
                //fprop.setVisible(true);
            }
        });
    }

    void openClient() {
        final Lobby flb = lb;
        //final Properties fprop = prop;
        final Client fcli = cli;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(false);
                fcli.setVisible(true);
                fcli.updateLabels();
                //fprop.setVisible(false);
            }
        });
    }

    void run(boolean gui) {
        this.startServiceRegistry();
        this.startServer();

        com = new Comunity(dbs);

        try {
            myIP = this.getIp();
        } catch (Exception ex) {
            myIP = JOptionPane.showInputDialog("Could not find your public IP. Please enter it (http://www.whatsmyip.org/)");
        }
        if (gui) {
            //this.setProp(new Properties(this));
            this.setLobby(new Lobby(this));
            this.setClient(new Client(this));

            //synchMap = new SynchReg();
            this.openLobby();
        }
    }

    public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void RegPeer(PeerReg peerReg) {
        dbs.println("Peer: RegPeer: Registering " + peerReg.getNick() + " with comunity");
        //System.out.println("Peer: RegPeer: Registering " + peerReg.getNick() + " with comunity");
        com.RegPeer(peerReg);
    }

    private void startServiceRegistry() {
        sr = new ServiceRegistry(this);
        executor.execute(sr);
    }

    void startServer() {
        pst = new PeerServerThread(this);
        executor.execute(pst);
    }

    void sendMsg(String host, int port, Message msg) {
        String uuid = com.getUUID();
        msg.setUUID(uuid);
        executor.execute(new PeerClientThread(host, port, msg, this));
    }

    //java.util.ConcurrentModificationException line 236?
    void sendMsgToComunity(Message msg) {
        //Sends a message to all other members of a comunity
        Map m = com.getComunityPeers();
        Set s = m.keySet(); // Needn't be in synchronized block
        String key;
        PeerReg opr;
        synchronized (m) {  // Synchronizing on m, not s!
            Iterator i = s.iterator(); // Must be in synchronized block
            while (i.hasNext()) {
                key = (String) i.next();
                opr = (PeerReg) m.get(key);
                if (!this.getMyIp().equals(opr.getAddr())) {
                    System.out.println("sending stream to a guy");
                    this.sendMsg(opr.getAddr(), 40, msg);
                }
            }
        }
    }

    void sendToHost(Message msg) {
        this.sendMsg(this.com.getHost(), 40, msg);
    }

    /*
     public SynchReg getSynchReg() {
     return synchMap;
     }
     */
    Lobby getLobby() {
        return lb;
    }

    Client getClient() {
        return cli;
    }

    void addKnownComunity(ComunityRegistration cm) {
        cMap.put(cm.getUUID(), cm);
    }

    void PeerToJoin(Message msg) {
        com.addPeer(msg.getName(), msg.getipAddr());
        msg.setType("Register Client");
        if (this.isHost()) {
            dbs.println("I am host and I am addinging: " + msg.getName() + " @" + msg.getipAddr());
            //System.out.println("I am host and I am addinging: " + msg.getName() + " @" + msg.getipAddr());

            if (!this.cli.isPlaylistEmpty()) {
                this.DeliverFirstStream(msg.getipAddr(), "demo");
            }

            Message sethostmsg = new Message();
            sethostmsg.setipAddr(this.getMyIp());
            sethostmsg.setName(this.com.getComunityName());
            sethostmsg.setType("Set Host");
            this.sendMsg(msg.getipAddr(), 40, sethostmsg);

            //send cMap to list to msg.getipAddr()
            this.sendMsgToComunity(msg);
            this.SendPMap(msg.getipAddr());
            

            //When a new client joins the Comunity it neads to know where the stream is currently
            this.DeliverPlaylist(msg.getipAddr());
        } else {

        }

        //If comunity Host register the peer
        //If not comunity Host, send this to comunity Host
    }

    void SendPMap(String ipAddr) {
        Map m = com.getComunityPeers();
        Set s = m.keySet(); // Needn't be in synchronized block
        String key;
        PeerReg opr;
        synchronized (m) {  // Synchronizing on m, not s!
            Iterator i = s.iterator(); // Must be in synchronized block
            while (i.hasNext()) {
                key = (String) i.next();
                opr = (PeerReg) m.get(key);

                Message msg = new Message();
                dbs.println("Adding " + opr.getNick() + " @" + opr.getAddr() + " to list of users to be sent to " + ipAddr);
                //System.out.println("Adding " + opr.getNick() + " @" + opr.getAddr() + " to list of users to be sent to " + ipAddr);
                msg.setipAddr(opr.getAddr());
                msg.setType("Register Client");
                msg.setName(opr.getNick());
                if (!opr.ipAddr.equals(ipAddr)) {
                    this.sendMsg(ipAddr, 40, msg);
                }
            }
        }
    }

    void DeliverStream(String ipAddr, String path) {
        //Delives a message that set where the stream is currently.
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Stream");
        msgret.setName(path);
        msgret.setText("video");
        this.sendMsg(ipAddr, 40, msgret);
    }

    private void DeliverFirstStream(String ipAddr, String path) {
        //Delives a message that set where the stream is currently.
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Set First Stream");
        msgret.setName(path);
        msgret.setText("video");
        this.sendMsg(ipAddr, 40, msgret);
    }

    void DeliverStreamToComunity(String ipAddr, String path, String mediaType) {
        //Delives a message that set where the stream is currently.
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Stream");
        msgret.setName(path);
        msgret.setText(mediaType);
        this.sendMsgToComunity(msgret);
    }

    boolean isHost() {
        dbs.println("isHost: " + this.getMyIp() + " and " + com.getHost());
        //System.out.println("isHost: " + this.getMyIp() + " and " + com.getHost());
        return this.getMyIp().equals(com.getHost());
    }

    String getHost() {
        return com.getHost();
    }

    void DeliverPlaylist(String ipAddr) {
        //Deliver the playlist... how?
        List li = null;
        PublicPlaylist pli = cli.getPublicPlaylist();
        pli.getLock().lock();
        try {
            //get media from li and put into a list.
            li = pli.getMediaList();

            pli.getCV().signalAll();
        } finally {
            pli.getLock().unlock();
        }
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Playlist");
        msgret.setList(li);
        this.sendMsg(ipAddr, 40, msgret);
    }

    void DeliverPlaylistToComunity() {
        //Deliver the playlist... how?
        List li = null;
        PublicPlaylist pli = cli.getPublicPlaylist();
        pli.getLock().lock();
        try {
            //get media from li and put into a list.
            li = pli.getMediaList();

            pli.getCV().signalAll();
        } finally {
            pli.getLock().unlock();
        }
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Playlist");
        msgret.setList(li);
        this.sendMsgToComunity(msgret);
    }

    void Ping(String ipAddr, String why) {
        dbs.println("Ping from: " + this.getMyIp() + " to " + ipAddr);
        //System.out.println("Ping from: " + this.getMyIp() + " to " + ipAddr);
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Ping");
        msgret.setText(why);
        this.sendMsg(ipAddr, 40, msgret);
    }

    void Pong(Message msg) {
        dbs.println("Pong from: " + this.getMyIp() + " to " + msg.getipAddr());
        //System.out.println("Pong from: " + this.getMyIp() + " to " + msg.getipAddr());
        String ipAddr = msg.getipAddr();
        Message msgret = new Message();
        switch (msg.getText()) {
            case "Move Host":
                msgret.setipAddr(this.getMyIp());
                msgret.setType("Pong");
                msgret.setText("Move Host");
                break;
        }
        this.sendMsg(ipAddr, 40, msgret);
    }

    void handlePong(Message msg) {
        dbs.println("Handling pong from: " + msg.getipAddr());
        //System.out.println("Handling pong from: " + msg.getipAddr());
        Message msgret = new Message();
        switch (msg.getText()) {
            case "Move Host":
                msgret.setipAddr(msg.getipAddr());
                msgret.setName(this.com.getComunityName());
                msgret.setType("Set Host");
                this.sendMsg(msg.getipAddr(), 40, msgret);
                break;
        }

    }

    void setComunityName(String name) {
        com.setComunityName(name);
    }

    void setHost(String ipAddr, String uuid) {
        isConnected = true;
        com.setHost(ipAddr);
        com.setUUID(uuid);
        dbs.println("Host changed to: " + ipAddr);
        //System.out.println("Host changed to: " + ipAddr);
        if (ipAddr.equals(this.getMyIp())) {
            this.cli.setHost(this.getMyIp());
            //send message to all with new host.
            Message msgret = new Message();
            msgret.setipAddr(ipAddr);
            msgret.setType("Set Host");

            this.sendMsgToComunity(msgret);

            //Reregister comunity. 
            this.registerComunity(com.getRegistryAddr(), 3333);

            if (!this.cli.isPlaylistEmpty()) {
                //clean start of playlist
                dbs.println("Calling cleanStartOfPlaylist");
                //System.out.println("Calling cleanStartOfPlaylist");
                this.cli.cleanStartOfPlaylist();

                //start stream
                dbs.println("Calling startStream");
                //System.out.println("Calling startStream");
                this.cli.startStream();
            }

        }
    }

    void connectionEvent(String ipAddr, String ce) {
        dbs.println("Peer.connectionEvent: Starting");
        //System.out.println("Peer.connectionEvent: Starting");

        if (ce.equals("connect")) {
            dbs.println("Peer.connectionEvent: Connection refused: connect");
            //System.out.println("Peer.connectionEvent: Connection refused: connect");

            if (isConnected) {
                if (this.isHost()) {
                    dbs.println("Peer.connectionEvent: isHost()");
                    //System.out.println("Peer.connectionEvent: isHost()");
                    dbs.println("PEER TO BE REMOVED: " + ipAddr);
                    //System.out.println("PEER TO BE REMOVED: " + ipAddr);
                    dbs.println("SIZE: " + com.pMap.size());
                    //System.out.println("SIZE: " + com.pMap.size());
                    removePeerbyIp(ipAddr);

                    //com.removePeerByIp(ipAddr);
                } else {
                    dbs.println("Peer.connectionEvent: else");
                    //System.out.println("Peer.connectionEvent: else");
                    //Check with other peers if the host is lost
                    ActionExitToLobby aetl = new ActionExitToLobby(this, cli.getStm(), cli.getMedaPlayer());
                    new Thread(aetl).start();
                    JOptionPane.showMessageDialog(lb, "Lost connection to community host.");
                }
            }

        }

    }

    void removePeerbyIp(String ipAddr) {
        dbs.println("Peer.removePeer: Starting removePeer");
        //System.out.println("Peer.removePeer: Starting removePeer");
        String nick = com.getNickByIp(ipAddr);
        removePeerbyNick(nick);
        /*
         if (!nick.equals("")) {

         //remove nick from comunity.
         System.out.println("Peer.removePeer: removing from comunity by nick: " + nick);

         com.removePeerByName(nick);

         //Remove ipAdd/nick from playlist
         System.out.println("Peer.removePeer: removing from playlist by nick: " + nick);
         cli.getPublicPlaylist().removefromPlaylist(nick);
         } else {
         System.out.println("Peer.removePeer: else");
         System.out.println("Tried to remove someone who did not exist");
         }
         */
    }

    void removePeerbyNick(String nick) {
        dbs.println("Peer.removePeer: Starting removePeer");
        //System.out.println("Peer.removePeer: Starting removePeer");

        dbs.println("Peer.removePeer: about to enter if with the nick " + nick);
        //System.out.println("Peer.removePeer: about to enter if with the nick " + nick);
        if (!nick.equals("")) {
            dbs.println("Peer.removePeer: nick not empty");
            //System.out.println("Peer.removePeer: nick not empty");
            dbs.println("Peer.removePeer: removing from comunity by nick: " + nick);
            //System.out.println("Peer.removePeer: removing from comunity by nick: " + nick);

            //remove nick from comunity. Dose not propagate to the rest of the comunity.
            com.removePeerByName(nick);

            //Do propagate to the rest of the comunity.
            if (this.isHost()) {
                dbs.println("Sending removePeerbyNick message");
                //System.out.println("Sending removePeerbyNick message");
                Message msg = new Message();
                msg.setName(nick);
                msg.setType("removePeerbyNick");
                this.sendMsgToComunity(msg);
            }

            //Remove ipAdd/nick from playlist
            dbs.println("Peer.removePeer: removing from playlist by nick: " + nick);
            //System.out.println("Peer.removePeer: removing from playlist by nick: " + nick);
            cli.getPublicPlaylist().removefromPlaylist(nick);
        } else {
            dbs.println("Peer.removePeer: nick is empty");
            //System.out.println("Peer.removePeer: nick is empty");

            dbs.println("Tried to remove someone who did not exist");
            //System.out.println("Tried to remove someone who did not exist");
        }

    }

    void removePeerFromCommunity(String nick) {
        dbs.println("Starting to remove host from community list!");
        //System.out.println("Starting to remove host from community list!");
        com.removePeerByName(nick);
    }

    void setisConnected(boolean b) {
        isConnected = b;
    }

    public DebugSys getDebugSys() {
        return dbs;
    }

}
