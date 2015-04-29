/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Pontus
 */
public class Comunity {

    String nick;
    String topic;
    String cname;
    String host;

    final Lock lock;
    final Condition ocupied;
    Map<String, PeerReg> pMap;

    public Comunity() {
        System.out.println("Initializing Comunity View");

        lock = new ReentrantLock();
        ocupied = lock.newCondition();
        this.topic = "";

        this.pMap = Collections.synchronizedMap(new HashMap<String, PeerReg>());
    }

    public void RegPeer(PeerReg peer) {
        System.out.println("Comunity: RegPeer: " + peer.getNick());
        pMap.put(peer.getAddr(), peer);
    }

    void create(String cname, String myIP, String topic, String nick) {
        System.out.println("Comunity: create: " + cname + ", " + topic + ", @" + myIP);

        lock.lock();
        try {
            this.cname = cname;
            this.host = myIP;
            this.topic = topic;

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
    }

    void setHost(String host) {
        this.host = host;
    }

    void setTopic(String topic) {
        lock.lock();
        try {
            this.topic = topic;

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
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

    Map getComunityPeers() {
        return pMap;
    }

    void addPeer(String nick, String ipAddr) {
        System.out.println("Comunity: Adding " + nick + " @" + ipAddr);
        pMap.put(ipAddr, new PeerReg(nick, ipAddr));
    }
}
