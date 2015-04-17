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

    Comunity com;
    ClientInterface cri;
    String nick;
    Lobby lb;
    Client cli;
    Properties prop;

    String myVlcPath;
    String myIP;

    public Peer() {

        com = new Comunity();

        try {
            cri = new ClientInterface(nick, com);
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void setNick(String me) {
        this.nick = me;
        com.setNick(this.nick);
        com.setCri(this.cri);
    }

    void createComunity(String cname, String topic) {
        //System.out.println("Peer: createComunity:" + cname + ", " + topic);
        com.create(cname, cri, topic, nick);
    }

    void registerComunity(String rhost, String registry, int port) {
        //System.out.println("Peer: registerComunity: " + registry + ", " + port);
        //registry = Sadocsynk
        //port = 1099
        com.Register(rhost, registry, port);
    }

    void registerComunity(String rhost, String registry) {
        //registry = Sadocsynk
        //port = 1099
        this.registerComunity(rhost, registry, 1099);
    }

    void registerComunity() {
        //registry = Sadocsynk
        //port = 1099
        this.registerComunity("localhost", "Sadocsynk", 1099);
    }

    void setComunityTopic(String topic) {
        //System.out.println("Peer: setComunityTopic:" + topic);
        com.setTopic(topic);
    }

    void findAllComunity(String rhost, String service, int port) {
        //System.out.println("Peer: findAllComunity:" + service);
        //port is the port where the rmi registry should be located.
        //registry is the addres to the registry where you whant to look for the service.

        //name is the name of the comunity we are looking for.
        //registry = Sadocsynk
        //port = 1099
        com.findAll(rhost, service, port, cri);
    }

    void joinComunity(String cname, String rhost, String service, int port) {
        //System.out.println("Peer: findComunity:" + cname);
        com.find(cname, cri, rhost, service, port);
    }

    String getNick() {
        return nick;
    }

    void setLobby(Lobby lb) {
        this.lb = lb;
        cri.setLobby(lb);
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(true);
                //cli.setVisible(false);
                fprop.setVisible(false);
            }
        });
    }

    void openProperties() {
        final Lobby flb = lb;
        final Properties fprop = prop;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                flb.setVisible(false);
                //cli.setVisible(false);
                fprop.setVisible(true);
            }
        });
    }

    String getMyIp() {
        return this.myIP;
    }

    void run() {
        prop = new Properties(this);
        lb = new Lobby(this);

        this.setProp(prop);
        this.setLobby(lb);
        this.openProperties();
    }

}
