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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class Comunity {

    final Lock lock;
    final Condition ocupied;

    //String nick;
    //ClientRemoteInterface mycri;
    String topic;
    String cname;
    String RegistryAddr;
    String host;
    //ClientRemoteInterface host;

    Map<String, PeerReg> pMap;

    public Comunity() {
        lock = new ReentrantLock();
        ocupied = lock.newCondition();

        System.out.println("Initializing Comunity View");
        this.topic = "";

        this.pMap = Collections.synchronizedMap(new HashMap<String, PeerReg>());
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

        lock.lock();
        try {
            pMap.put(peer.getNick(), peer);

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
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

        //Now you have to join a comunity that you create.
        //RegPeer(nick, new PeerReg(nick, host));
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
    /*
     void find(String cname, ClientRemoteInterface cri, String rhost, String sname, int port) {
     RegistryConnecter rc = new RegistryConnecter(rhost, sname, port);

     if (rc.Connect()) {
     rc.getComunity(cname, cri);
     };
     }
     */

    void setTopic(String topic) {
        lock.lock();
        try {
            this.topic = topic;

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }

    }
    /*
     void findAll(String rhost, String name, int port, ClientRemoteInterface cri) {
     RegistryConnecter rc = new RegistryConnecter(rhost, name, port);

     if (rc.Connect()) {
     rc.getAll(cri);
     };
     }
     */
    /*
     void setNick(String nick) {
     lock.lock();
     try {
     this.nick = nick;

     ocupied.signalAll();
     } finally {
     lock.unlock();
     }

     }
     */

    String getComunityName() {

        String ret;
        lock.lock();
        try {

            ret = cname;
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    String getHost() {
        String ret;
        lock.lock();
        try {

            ret = host;
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    String getTopic() {
        String ret;
        lock.lock();
        try {

            ret = topic;
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;

    }

    Map getComunityPeers() {
        Map ret;
        lock.lock();
        try {

            ret = pMap;
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    void addPeer(String name, String ipAddr) {
        System.out.println("Comunity: addPeer: Adding " + name + " @" + ipAddr);
        lock.lock();
        try {

            pMap.put(name, new PeerReg(name, ipAddr));

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }

    }

    String getPeerIP(String nowPlayingOwner) {
        return pMap.get(nowPlayingOwner).getAddr();
    }

    void setRegistryAddr(String rhost) {
        this.RegistryAddr = rhost;
    }

    String getRegistryAddr() {
        return RegistryAddr;
    }

    void setComunityName(String name) {
        this.cname = name;
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
                it.remove();
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
