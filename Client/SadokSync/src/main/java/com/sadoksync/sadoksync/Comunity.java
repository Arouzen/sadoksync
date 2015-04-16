/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class Comunity {
    String nick;
    ClientRemoteInterface mycri;
    
    String topic;
    String cname;
    RegistryConnecter rc;

    ClientRemoteInterface host;
    
    PeerMap pMap;
    
    public Comunity(){
        this.topic = "";
        this.pMap = new PeerMap();
    }
    
    public void Register(String rhost, String name, int port) {
        //System.out.println("Comunity: Register: " + name + ", " + port);
        rc = new RegistryConnecter(rhost, name, port);
        //rc = new RegistryConnecter();
        
        //"localhost"
        
        if (rc.Connect()) {
            rc.register(cname, this.host, topic);
        };
    }

    public void RegPeer(String nick, PeerReg peer) {
        System.out.println("Comunity: RegPeer: " + nick);
        //TO DO: Send registration of user to all other clients if this should be done. 
        pMap.put(nick, peer);
    }

    void create(String cname, ClientRemoteInterface host, String topic, String nick) {
        //System.out.println("Comunity: create: " + cname + ", " + topic);
        this.cname = cname;
        this.host = host;
        this.topic = topic;
        //Now you have to join a comunity that you create.
        //RegPeer(nick, new PeerReg(nick, host));
    }
    
    void setHost(ClientRemoteInterface host){
        this.host = host;
        try {
            host.register(nick, mycri);
        } catch (RemoteException ex) {
            Logger.getLogger(Comunity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void find(String cname, ClientRemoteInterface cri, String rhost, String sname, int port) {
        RegistryConnecter rc = new RegistryConnecter(rhost, sname, port);

        if (rc.Connect()) {
            rc.getComunity(cname, cri);
        };
    }

    void setTopic(String topic) {
        this.topic = topic;
    }

    void findAll(String rhost, String name, int port, ClientRemoteInterface cri) {
        RegistryConnecter rc = new RegistryConnecter(rhost, name, port);

        if (rc.Connect()) {
            rc.getAll(cri);
        };
    }

    void setNick(String nick) {
        this.nick = nick;
    }

    void setCri(ClientInterface cri) {
         this.mycri = cri;
    }
}
