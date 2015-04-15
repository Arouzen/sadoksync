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
    ClientRemoteInterface cri;
    String nick;

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
    }

    void createComunity(String name, String topic) {
        System.out.println("Peer: createComunity:" + name + ", " + topic);
        com.create(name, cri, topic);
    }

    void registerComunity(String registry, int port) {
        System.out.println("Peer: registerComunity: " + registry + ", " + port);
        //registry = Sadocsynk
        //port = 1099
        com.Register();
    }
    void registerComunity(String registry) {
        //registry = Sadocsynk
        //port = 1099
        this.registerComunity(registry, 1099);
    }
        void registerComunity() {
        //registry = Sadocsynk
        //port = 1099
        this.registerComunity("Sadocsynk", 1099);
    }
    
    
    void setComunityTopic(String topic) {
        System.out.println("Peer: setComunityTopic:" + topic);
        com.setTopic(topic);
    }

    void findAllComunity(String registry, int port) {
        System.out.println("Peer: findAllComunity:" + registry);
        //port is the port where the rmi registry should be located.
        //registry is the addres to the registry where you whant to look for the service.

        //name is the name of the comunity we are looking for.
        //registry = Sadocsynk
        //port = 1099
        com.findAll(cri);
    }

    void findAllComunity(String host) {
        this.findAllComunity(host, 1099);
    }

    void findAllComunity() {
        this.findAllComunity("Sadoksync", 1099);
    }

    void findComunity(String name, String registry, int port) {
        System.out.println("Peer: findComunity:" + name);
        //port is the port where the rmi registry should be located.
        //registry is the addres to the registry where you whant to look for the service.

        //name is the name of the comunity we are looking for.
        //registry = Sadocsynk
        //port = 1099
        com.find(name, cri);
    }

    void findComunity(String name, String host) {
        findComunity(name, host, 1099);
    }

    void findComunity(String name) {
        findComunity(name, "Sadoksync", 1099);
    }

    String getNick() {
        return nick;
    }

}
