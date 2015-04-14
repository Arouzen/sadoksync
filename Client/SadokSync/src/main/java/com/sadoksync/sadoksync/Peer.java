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
    String name;

    public Peer() {

        com = new Comunity();

        try {
            cri = new ClientInterface(name, com);
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setName(String me) {
        this.name = me;
    }

    void createComunity() {
        com.create(name, cri);
    }

    void findComunity(String name, String registry, int port) {
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
        findComunity(name,"Sadoksync", 1099);
    }

    String getName() {
        return name;
    }
}
