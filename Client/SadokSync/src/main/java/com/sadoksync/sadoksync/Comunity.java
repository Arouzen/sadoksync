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

    String getPeerIP(String nowPlayingOwner) {
        return pMap.get(nowPlayingOwner).getAddr();
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

    void removePeerByIp(String ip) {
        Iterator it = pMap.entrySet().iterator();
        lock.lock();
        try {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                PeerReg p = (PeerReg) pair.getValue();
                if (p.getAddr().equals(ip)) {
                    pMap.remove(pair.getKey());
                }
                it.remove();
            }
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
    }

    String getNickByIp(String ipAddr) {
        Iterator it = pMap.entrySet().iterator();
        String ret = "";
        lock.lock();
        try {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                PeerReg p = (PeerReg) pair.getValue();
                if (p.getAddr().equals(ipAddr)) {
                    ret = pMap.get(pair.getKey()).getNick();
                }
                //it.remove();
            }
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    void removePeerByName(String nick) {
        lock.lock();
        try {
            System.out.println(pMap.size());
            Iterator it = pMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println("BEFORE: " + pair.getKey());
            }
            pMap.remove(nick);
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
}
