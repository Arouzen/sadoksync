/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class Peer {

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

    public Peer() {

    }

    void setNick(String me) {
        this.nick = me;
        com.setNick(this.nick);
        //com.setCri(this.cri);
    }

    void createComunity(String cname, String topic) {
        com.create(cname, myIP, topic, nick);
    }

    void registerComunity(String rhost, int port) {
        Message msg = new Message(com);

        this.sendMsg(rhost, msg);
        //call client to start streaming
        // cli.startStreamingServer("C:\\pontus\\studier\\ID1003ProjIT\\sample\\test.mp4");

        //com.Register(rhost, registry, port);
/*
         RegistryConnecter rc = new RegistryConnecter(rhost, registry, port);

         if (rc.Connect()) {
         rc.register(com);
         };
         */
    }

    void setComunityTopic(String topic) {
        //System.out.println("Peer: setComunityTopic:" + topic);
        com.setTopic(topic);
    }

    void findAllComunity(String rhost, int port) {
        Message msg = new Message();
        msg.setipAddr(this.getMyIp());
        msg.setType("Find All");
        msg.setName(this.getNick());
        this.sendMsg(rhost, msg);

        //com.findAll(rhost, service, port, cri);
/*
         RegistryConnecter rc = new RegistryConnecter(rhost, registry, port);

         if (rc.Connect()) {
         rc.getAll(cri);
         };
         */
    }

    void joinComunity(String cname, String rhost, String registry, int port) {
        //System.out.println("Peer: findComunity:" + cname);
        //com.find(cname, cri, rhost, service, port);

        RegistryConnecter rc = new RegistryConnecter(rhost, registry, port);

        if (rc.Connect()) {
            rc.getComunity(cname, cri);
        };
    }

    String getNick() {
        return nick;
    }

    void setLobby(Lobby lb) {
        this.lb = lb;
        //cri.setLobby(lb);
    }

    void setProp(Properties prop) {
        this.prop = prop;
    }

    void setClient(Client cli) {
        this.cli = cli;
        cri.setClient(cli);
    }

    void setMyIP(String ipAddr) {
        this.myIP = ipAddr;
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

    String getMyIp() {
        return this.myIP;
    }

    String getMyVlc() {
        return myVlcPath;
    }

    void run() {
        this.startServer();

        com = new Comunity();

        /*
         try {
         cri = new ClientInterface(this);
         } catch (RemoteException | MalformedURLException ex) {
         Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
         }
         */
        cli = new Client(this);
        prop = new Properties(this);
        lb = new Lobby(this);

        this.setProp(prop);
        this.setLobby(lb);
        this.openProperties();
    }

    void RegPeer(PeerReg peerReg) {
        System.out.println("Peer: RegPeer: Registering " + peerReg.getName() + " with comunity");
        com.RegPeer(peerReg);
    }

    void setComunityHost(ClientRemoteInterface rri) {
        System.out.println("Peer: setComunityHost");
        this.openClient();
        com.setHost(rri);
        try {
            rri.register(this.getNick(), this.getMyCri(), this.getMyIp());
        } catch (RemoteException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ClientRemoteInterface getMyCri() {
        return cri;
    }

    void setMedia(String myIp) {
        if (myIp.equals(this.getMyIp())) {
            cli.setMedia("127.0.0.1");
        } else {
            cli.setMedia(myIp);
        }

    }

    void setMySR(ServiceRegistry serviceRegistry) {
        sr = serviceRegistry;
    }

    void startServer() {
        pst = new PeerServerThread(this);
        pst.start();
    }

    void sendMsg(String host, Message msg) {
        PeerClientThread pct = new PeerClientThread(host, msg);
        pct.start();
    }

}
