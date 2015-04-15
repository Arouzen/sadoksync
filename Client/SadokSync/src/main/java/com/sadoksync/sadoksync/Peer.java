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

    void createComunity(String cname, String topic) {
        System.out.println("Peer: createComunity:" + cname + ", " + topic);
        com.create(cname, cri, topic, nick);
    }

    void registerComunity(String rhost, String registry, int port) {
        System.out.println("Peer: registerComunity: " + registry + ", " + port);
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
        System.out.println("Peer: setComunityTopic:" + topic);
        com.setTopic(topic);
    }

    void findAllComunity(String rhost, String service, int port) {
        System.out.println("Peer: findAllComunity:" + service);
        //port is the port where the rmi registry should be located.
        //registry is the addres to the registry where you whant to look for the service.

        //name is the name of the comunity we are looking for.
        //registry = Sadocsynk
        //port = 1099
        com.findAll(rhost, service, port, cri);
    }

    void findComunity(String sname, String rhost, String service, int port) {
        System.out.println("Peer: findComunity:" + sname);

        //name is the name of the comunity we are looking for.
        //rhost = localhost
        //service = Sadocsynk
        //port = 1099
        com.find(rhost, service, port, sname, cri);
    }

    String getNick() {
        return nick;
    }

}
