/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;

/**
 *
 * @author Pontus
 */
public class Comunity {

    String topic;
    RegistryConnecter rc;

    ClientRemoteInterface host;

    PeerMap pMap;
    
    public Comunity(){
        this.topic = "";
        this.pMap = new PeerMap();
    }
    
    public void Register(String name, int port) {
        System.out.println("Comunity: Register: " + name + ", " + port);
        //rc = new RegistryConnecter(name, port);
        rc = new RegistryConnecter();
        
        if (rc.Connect()) {
            rc.register(name, host, topic);
        };
    }

    public void Register(String name) {
        //registry = Sadocsynk
        //port = 1099
        this.Register(name, 1099);
    }

    public void Register() {
        //registry = Sadocsynk
        //port = 1099
        this.Register("Sadocsynk", 1099);
    }

    public void RegPeer(String namn, PeerReg peer) {
        System.out.println("Comunity: RegPeer: " + namn);
        pMap.put(namn, peer);
    }

    void create(String namn, ClientRemoteInterface host, String topic) {
        System.out.println("Comunity: create: " + namn + ", " + topic);
        this.host = host;
        this.topic = topic;
        RegPeer(namn, new PeerReg(namn, host));
    }
    
    void setHost(ClientRemoteInterface host){
        this.host = host;
    }

    void find(String name, ClientRemoteInterface cri) {
        RegistryConnecter rc = new RegistryConnecter();

        if (rc.Connect()) {
            rc.getComunity(name, cri);
        };
    }

    void setTopic(String topic) {
        this.topic = topic;
    }

    void findAll(ClientRemoteInterface cri) {
        RegistryConnecter rc = new RegistryConnecter();

        if (rc.Connect()) {
            rc.getAll(cri);
        };
    }
}
