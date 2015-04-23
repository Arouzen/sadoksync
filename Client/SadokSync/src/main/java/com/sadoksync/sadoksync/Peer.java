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

    Comunity com;

    //ClientInterface cri;
    ServiceRegistry sr;
    Lobby lb;
    Client cli;
    Properties prop;

    //SynchReg synchMap;
    Map<String, ComunityRegistration> cMap;

    public Peer() {
        executor = Executors.newFixedThreadPool(50);
        this.cMap = Collections.synchronizedMap(new HashMap<String, ComunityRegistration>());
    }

    //del?
    void setComunityTopic(String topic) {
        //System.out.println("Peer: setComunityTopic:" + topic);
        com.setTopic(topic);
    }

    //del?
    void createComunity(String cname, String topic) {
        com.create(cname, myIP, topic, nick);
    }

    void registerComunity(String rhost, int port) {
        Message msg = new Message(com);
        this.sendMsg(rhost, 3333, msg);
    }

    void findAllComunity(String rhost, int port) {
        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Find All");
        msg.setName(this.getNick());
        this.sendMsg(rhost, 3333, msg);
    }
 
    void joinComunity(String cname) {
        ComunityRegistration cr = cMap.get(cname);

        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Join Comunity");
        msg.setName(this.getMyIp());
        //msg.setText(cname);
        System.out.println("joinComunity");
        this.sendMsg(cr.getHost(), 4444, msg);
        this.openClient();
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

    void openProperties() {
        final Lobby flb = lb;
        final Properties fprop = prop;
        final Client fcli = cli;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(false);
                fcli.setVisible(false);
                fprop.setVisible(true);
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

    void RegPeer(PeerReg peerReg) {
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
                System.out.println(this.getMyIp() + " ? " + opr.getAddr());
                if (!this.getMyIp().equals(opr.getAddr())) {
                    System.out.println("Trigger");
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

    void PeerToJoin(Message msg) {
        com.addPeer(msg.getName(), msg.getipAddr());
        msg.setType("Register Client");
        if (this.isHost()) {
            System.out.println("This is host");
            this.DeliverStream(msg.getipAddr(), "demo");

            //send cMap to list to msg.getipAddr()
            this.SendPMap(msg.getipAddr());
            this.sendMsgToComunity(msg);

            //When a new client joins the Comunity it neads to know where the stream is currently
            this.DeliverPlaylist(msg.getipAddr());
        } else {

        }

        //If comunity Host register the peer
        //If not comunity Host, send this to comunity Host
    }

    void SendPMap(String ipAddr) {
        Map m = com.getComunityPeers();
        System.out.println("SendPMap");
        Set s = m.keySet(); // Needn't be in synchronized block
        String key;
        PeerReg opr;
        synchronized (m) {  // Synchronizing on m, not s!
            Iterator i = s.iterator(); // Must be in synchronized block
            while (i.hasNext()) {
                
                key = (String) i.next();
                opr = (PeerReg) m.get(key);
                Message msg = new Message();

                msg.setipAddr(opr.getAddr());
                msg.setType("Register Client");
                msg.setName(opr.getNick());
                System.out.println(opr.getNick() + "adding");

                 if (!this.getMyIp().equals(ipAddr)) {
                   this.sendMsg(ipAddr, 4444, msg);
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
        this.sendMsg(ipAddr, 4444, msgret);
    }

    void DeliverStreamToComunity(String ipAddr, String path) {
        //Delives a message that set where the stream is currently.
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Stream");
        msgret.setName(path);
        this.sendMsgToComunity(msgret);
    }

    boolean isHost() {
        System.out.println("IsHost" + com.getHost() + " ? " + this.getMyIp() + " " + this.getMyIp().equals(com.getHost()));
        return this.getMyIp().equals(com.getHost());
    }

    String getHost() {
        return com.getHost();
    }

    void DeliverPlaylist(String ipAddr) {
        //Deliver the playlist... how?
        List li = null;
        PublicPlaylist pli = cli.getPubicPlaylist();
        pli.getLock().lock();
        try{
            //get media from li and put into a list.
            li = pli.getMediaList();
            
            pli.getCV().signalAll();
        }finally{
            pli.getLock().unlock();
        }
        Message msgret = new Message();
        msgret.setipAddr(this.getMyIp());
        msgret.setType("Set Playlist");
        msgret.setList(li);
        this.sendMsg(ipAddr, 4444, msgret);
    }
}
