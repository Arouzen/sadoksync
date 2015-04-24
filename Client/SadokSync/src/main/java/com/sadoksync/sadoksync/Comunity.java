/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pontus
 */
public class Comunity {

    String nick;
    String topic;
    String cname;
    String host;
    

    Map<String,PeerReg> pMap;

    public Comunity() {
        System.out.println("Initializing Comunity View");
        this.topic = "";
 
        this.pMap = Collections.synchronizedMap(new HashMap<String,PeerReg>());
    }

    public void RegPeer(PeerReg peer) {
        System.out.println("Comunity: RegPeer: " + peer.getNick());
        //TO DO: Send registration of user to all other clients if this should be done. 
        pMap.put(peer.getAddr(), peer);
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

    void setTopic(String topic) {
        this.topic = topic;
    }

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

    void addPeer(String ipAddr, String nick) {
        System.out.println("Comunity: Adding " + nick + " @" + ipAddr);
        pMap.put(ipAddr, new PeerReg(nick, ipAddr));
    }
}
