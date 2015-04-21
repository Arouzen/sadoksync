/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class Comunity {

    String nick;
    //ClientRemoteInterface mycri;

    String topic;
    String cname;

    String host;
    //ClientRemoteInterface host;

    Map<String,PeerReg> pMap;

    public Comunity() {
        System.out.println("Initializing Comunity View");
        this.topic = "";
 
        this.pMap = Collections.synchronizedMap(new HashMap<String,PeerReg>());
    }
    /*
     public void Register(String rhost, String name, int port) {
     //System.out.println("Comunity: Register: " + name + ", " + port);
     rc = new RegistryConnecter(rhost, name, port);
     //rc = new RegistryConnecter();

     //"localhost"
     if (rc.Connect()) {
     rc.register(cname, this.host, topic);
     };
     }
     */

    public void RegPeer(PeerReg peer) {
        System.out.println("Comunity: RegPeer: " + peer.getNick());
        //TO DO: Send registration of user to all other clients if this should be done. 
        pMap.put(peer.getNick(), peer);
    }

    void create(String cname, String myIP, String topic, String nick) {
        System.out.println("Comunity: create: " + cname + ", " + topic +", @" + myIP);
        this.cname = cname;
        this.host = myIP;
        this.topic = topic;
 
        //Now you have to join a comunity that you create.
        //RegPeer(nick, new PeerReg(nick, host));
    }

    void setHost(String host) {
        this.host = host;
    }
    /*
     void find(String cname, ClientRemoteInterface cri, String rhost, String sname, int port) {
     RegistryConnecter rc = new RegistryConnecter(rhost, sname, port);

     if (rc.Connect()) {
     rc.getComunity(cname, cri);
     };
     }
     */

    void setTopic(String topic) {
        this.topic = topic;
    }
    /*
     void findAll(String rhost, String name, int port, ClientRemoteInterface cri) {
     RegistryConnecter rc = new RegistryConnecter(rhost, name, port);

     if (rc.Connect()) {
     rc.getAll(cri);
     };
     }
     */

    void setNick(String nick) {
        this.nick = nick;
    }



    String getComunityName() {
        return cname;
    }

    String getHost() {
        return host;
    }

    String getTopic() {
        return topic;
    }

    Map getComunityPeers(){
        return pMap;
    }

    void addPeer(String name, String ipAddr) {
        System.out.println("Comunity: addPeer: Adding " + name + " @" + ipAddr);
        pMap.put(name, new PeerReg(name, ipAddr));       
    }
}
