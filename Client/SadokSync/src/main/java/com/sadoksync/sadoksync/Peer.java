/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    //ServiceRegistry sr; 
    Lobby lb;
    Client cli;
    Properties prop;

    SynchReg synchMap;
    
    public Peer() {
        executor = Executors.newFixedThreadPool(50);
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
        this.sendMsg(rhost, msg);
    }

    void findAllComunity(String rhost, int port) {
        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Find All");
        msg.setName(this.getNick());
        this.sendMsg(rhost, msg);
    }

    void joinComunity(String cname, String addr, int port) {
        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Join Comunity");
        msg.setName(this.getNick());
        msg.setText(cname);

        this.sendMsg(addr, msg);
    }

    void setComunityHost(String host) {
        System.out.println("Peer: setComunityHost");

        this.openClient();
        com.setHost(host);

        this.joinComunity(com.getComunityName(), host, 4444);
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

    String getMyVlc() {
        return myVlcPath;
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
        this.startServer();

        com = new Comunity();

        this.setProp(new Properties(this));
        this.setLobby(new Lobby(this));
        this.setClient(new Client(this));

        synchMap = new SynchReg();
        
        this.openProperties();
    }

    void RegPeer(PeerReg peerReg) {
        System.out.println("Peer: RegPeer: Registering " + peerReg.getName() + " with comunity");
        com.RegPeer(peerReg);
    }

    void setMedia(String myIp) {
        if (myIp.equals(this.getMyIp())) {
            cli.setMedia("127.0.0.1");
        } else {
            cli.setMedia(myIp);
        }

    }

    void startServer() {
        pst = new PeerServerThread(this);
        executor.execute(pst);
    }

    void sendMsg(String host, Message msg) {
        executor.execute(new PeerClientThread(host, msg));
    }

    public SynchReg getSynchReg() {
        return synchMap;
    }

}
