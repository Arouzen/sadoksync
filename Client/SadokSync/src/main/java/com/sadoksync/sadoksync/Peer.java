/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Pontus
 */
public class Peer {

    ExecutorService executor;
    PeerServerThread pst;

    String nick;
    String myVlcPath;
    String myIP;
    Boolean isHost;
    Comunity com;

    //ClientInterface cri;
    ServiceRegistry sr;
    Lobby lb;
    Client cli;
    Properties prop;

    //SynchReg synchMap;
    Map<String, ComunityRegistration> cMap;

    public Peer() {
        isHost = false;
        myIP = "127.0.0.1";
        executor = Executors.newFixedThreadPool(50);
        this.cMap = Collections.synchronizedMap(new HashMap<String, ComunityRegistration>());
    }

    //del?
    void setComunityTopic(String topic) {

        com.setTopic(topic);
    }

    //del?
    void createComunity(String cname, String topic) {
        System.out.println("cname: " + cname + ",myIp: " + myIP + "topic: " + topic + "nick: " + nick);
        com.create(cname, myIP, topic, nick);
    }

    void registerComunity(String rhost, int port) {
        com.setRegistryAddr(rhost);
        Message msg = new Message(com);
        this.sendMsg(rhost, 3333, msg);
    }

    void findAllComunity(String rhost, int port) {
        Message msg = new Message();
        System.out.print("Find Community: " + rhost);
        //msg.setipAddr(this.getMyIp());
        msg.setType("Find All");
        msg.setName(this.getNick());
        this.sendMsg(rhost, 3333, msg);
    }

    void joinComunity(String cname, String addr, String nick) {

        Message msg = new Message();
        msg.setType("Join Comunity");
        msg.setName(nick);
        msg.setText(addr);
        //msg.setText(cname);
        System.out.println("JoiningComunity");
        this.sendMsg(addr, 4444, msg);
        this.openClient();
    }

    void confirmJoin(String ip) {
        System.out.println("Confirming join from: " + ip);
        Message msg = new Message();
        msg.setType("Join Confirmed");
        msg.setText(ip);
        this.sendMsg(ip, 4444, msg);
    }

    void setLobby(Lobby lb) {
        this.lb = lb;
    }

    void setProp(Properties prop) {
        this.prop = prop;
    }

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
        System.out.println("Setting ip to: " + ipAddr);
        this.myIP = ipAddr;
    }

    String getMyIp() {
        return this.myIP;
    }

    void setMyVlcPath(String vlcpath) {
        this.myVlcPath = vlcpath;
    }

    void openLobby() {
        final Lobby flb = lb;
        final Properties fprop = prop;
        final Client fcli = cli;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(true);
                fcli.setVisible(false);
                fprop.setVisible(false);
            }
        });
    }

    void directConnect(String ip) {

        Message msg = new Message();
        msg.setType("Join Comunity");
        msg.setName(this.getNick());
        //msg.setText(cname);

        this.sendMsg(ip, 4444, msg);
        this.openClient();
    }

    void openProperties() {
        //Kill stuff here
        final Lobby flb = lb;
        final Properties fprop = prop;
        final Client fcli = cli;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(true);
                fcli.setVisible(false);
                fprop.setVisible(false);
            }
        });
    }

    void openClient() {
        final Lobby flb = lb;
        final Properties fprop = prop;
        final Client fcli = cli;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(false);
                fcli.setVisible(true);
                fprop.setVisible(false);
            }
        });
    }

    void run() {
        this.stertServiceRegistry();
        this.startServer();

        com = new Comunity();

        this.setProp(new Properties(this));
        this.setLobby(new Lobby(this));
        this.setClient(new Client(this));

        //synchMap = new SynchReg();
        this.openProperties();
    }

    void regPeer(PeerReg peerReg) {
        System.out.println("Peer: RegPeer: Registering " + peerReg.getNick() + " with comunity");
        com.RegPeer(peerReg);
    }

    private void stertServiceRegistry() {
        sr = new ServiceRegistry(this);
        executor.execute(sr);
    }

    void startServer() {
        pst = new PeerServerThread(this);
        executor.execute(pst);
    }

    void sendMsg(String host, int port, Message msg) {
        System.out.println("sendMsg: " + host);
        executor.execute(new PeerClientThread(host, port, msg));
    }

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

                //Work is needed here
                if (!this.getMyIp().equals(opr.getAddr())) {
                    System.out.println("sendMsgToComunity(loop):  " + opr.getAddr() + " From:" + this.getMyIp());
                    //if (!this.isHost()) {
                    System.out.println("Trigger: ");
                    this.sendMsg(opr.getAddr(), 4444, msg);
                }
            }
        }
    }

    Lobby getLobby() {
        return lb;
    }

    Client getClient() {
        return cli;
    }

    void addKnownComunity(ComunityRegistration cm) {
        cMap.put(cm.getName(), cm);
    }

    void connectionEvent(String ipAddr, String ce) {
        System.out.println("Peer.connectionEvent: Starting");

        if (ce.equals("connect")) {
            System.out.println("Peer.connectionEvent: Connection refused: connect");

            if (this.isHost()) {
                System.out.println("Peer.connectionEvent: isHost()");
                System.out.println("PEER TO BE REMOVED: " + ipAddr);
                System.out.println("SIZE: " + com.pMap.size());
                removePeerFromCommunity(ipAddr);

                //com.removePeerByIp(ipAddr);
            } else {
                System.out.println("Peer.connectionEvent: else");
                //Check with other peers if the host is lost
            }
        }

    }

    //Add ip here   

    void peerToJoin(Message msg, String ipAdr) {
        if (this.isHost()) {
            System.out.println("This is host. Sending to community");
            com.addPeer(msg.getName(), ipAdr);

            msg.setType("Register");
            msg.setText(ipAdr);

            this.deliverStream(ipAdr, "demo");

            this.sendPMap(ipAdr);

            this.sendMsgToComunity(msg);
            //When a new client joins the Comunity it neads to know where the stream is currently
            this.deliverPlaylist(ipAdr);
        } else {
            System.out.println("This can only be called as Host");
        }
        //If comunity Host register the peer
        //If not comunity Host, send this to comunity Host
    }

    void ping(String ipAddr, String why) {
        System.out.println("Ping from: " + this.getMyIp() + " to " + ipAddr);
        Message msgret = new Message();
        msgret.setType("Ping");
        msgret.setText(why);
        this.sendMsg(ipAddr, 4444, msgret);
    }

    void Pong(Message msg, String ip) {
        System.out.println("Pong from: " + this.getMyIp() + " to " + ip);
        Message msgret = new Message();
        switch (msg.getText()) {
            case "Move Host":

                msgret.setType("Pong");
                msgret.setText("Move Host");
                break;
        }
        this.sendMsg(ip, 4444, msgret);
    }

    void handlePong(Message msg, String ip) {
        System.out.println("Handling pong from: " + ip);
        Message msgret = new Message();
        switch (msg.getText()) {
            case "Move Host":
                msgret.setName(this.com.getComunityName());
                msgret.setType("Set Host");
                this.setHostB(false);
                this.sendMsg(ip, 4444, msgret);
                break;
        }
    }

    void sendPMap(String ipAdr) {
        if (this.isHost()) {
            Map m = com.getComunityPeers();
            System.out.println("SendPMap:");
            Set s = m.keySet(); // Needn't be in synchronized block
            String key;
            PeerReg opr;
            Message msg = new Message();
            msg.setType("Register");

            synchronized (m) {  // Synchronizing on m, not s!
                Iterator i = s.iterator(); // Must be in synchronized block
                while (i.hasNext()) {

                    key = (String) i.next();
                    opr = (PeerReg) m.get(key);

                    //msg.setipAddr(opr.getAddr());
                    msg.setName(opr.getNick());
                    msg.setText(opr.getAddr());

                    if (!opr.getAddr().equals(this.getMyIp())) {
                        System.out.println("sending Register to: " + opr.getAddr() + ":: N =" + opr.getNick() + " ip=" + ipAdr);
                        this.sendMsg(ipAdr, 4444, msg);
                    }
                }
            }
        }
    }

    void deliverStream(String ipAddr, String path) {
        //Delives a message that set where the stream is currently.
        Message msgret = new Message();
        //msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Stream");
        msgret.setName(path);
        this.sendMsg(ipAddr, 4444, msgret);
    }

    void deliverStreamToComunity(String mediaType) {
        //Delives a message that set where the stream is currently.
        System.out.println("deliverPlaylistToComunity()");
        Message msgret = new Message();
        // msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Stream");
        msgret.setName("demo");
        msgret.setText(mediaType);
        this.sendMsgToComunity(msgret);
    }

    void deliverPlaylistToComunity() {
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
        //msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Playlist");
        msgret.setList(li);
        this.sendMsgToComunity(msgret);
    }

    boolean isHost() {
        return this.isHost;
    }

    void setHost(String ipAddr) {
        com.setHost(ipAddr);
        System.out.println("Host changed to: " + ipAddr);
        if (ipAddr.equals(this.getMyIp())) {
            this.cli.setHost(this.getMyIp());
            //send message to all with new host.
            Message msgret = new Message();

            msgret.setType("Set Host");

            this.sendMsgToComunity(msgret);

            //Reregister comunity. 
            this.registerComunity(com.getRegistryAddr(), 3333);

            if (!this.cli.isPlaylistEmpty()) {
                //clean start of playlist
                System.out.println("Calling cleanStartOfPlaylist");
                this.cli.cleanStartOfPlaylist();

                //start stream
                System.out.println("Calling startStream");
                this.cli.startStream();
            }

        }
    }
    void setHostB(Boolean isHost) {
        this.isHost = isHost;
    }

    String getHost() {
        return com.getHost();
    }

    void deliverPlaylist(String ipAddr) {
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
        //msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Playlist");
        msgret.setList(li);
        this.sendMsg(ipAddr, 4444, msgret);
    }

    void removePeerFromCommunity(String ip) {
        com.removePeer(ip);
    }
}
