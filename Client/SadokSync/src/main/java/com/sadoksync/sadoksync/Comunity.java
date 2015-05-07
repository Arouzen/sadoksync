/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
    String RegistryAddr;
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

    public boolean isEmpty() {
        boolean ret;
        lock.lock();
        try {

            if (pMap.size() == 0) {
                ret = true;
            } else {
                ret = false;
            }

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    void setHost(String host) {
        lock.lock();
        try {
            this.host = host;
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
    }

    String getRegistryAddr() {
        return RegistryAddr;
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

    void setRegistryAddr(String rhost) {
        System.out.println("Community: Setting RegistryAddr to: " +rhost);
        this.RegistryAddr = rhost;
    }

    void removePeer(String ip) {
        lock.lock();
        try {
            System.out.println(pMap.size());
            Iterator it = pMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println("BEFORE: " + pair.getKey());
            }
            pMap.remove(ip);
            Iterator it2 = pMap.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair = (Map.Entry) it2.next();

                System.out.println("AFTER: " + pair.getKey());
            }
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
    }

    void clearOldCommunity() {
        this.topic = "";
        this.pMap = Collections.synchronizedMap(new HashMap<String, PeerReg>());
        this.host = "";
    }
    public String getNextPeerIP() {
        String ret;
        lock.lock();
        try {
            System.out.println(pMap.size());
            Iterator it = pMap.entrySet().iterator();

            Map.Entry pair = (Map.Entry) it.next();
            PeerReg pr = (PeerReg)pair.getValue();
            ret = pr.getAddr();
            System.out.println("[Community] get next IP: " + pr.getAddr());
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;
    }
}
