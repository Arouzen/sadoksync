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

    RegistryConnecter rc;

    ClientRemoteInterface host;

    //Datastructur som håller ClientRemoteInterface
    SynchReg cliMap;
   
    public void Register(String name, int port) {
        rc = new RegistryConnecter(name, port);

        if (rc.Connect()) {
            rc.register(name, host);
        };
    }

    public void Register(String name) {
        rc = new RegistryConnecter(name);
    }

    public void Register() {
        rc = new RegistryConnecter();
    }

    public void RegPeer(String namn, ClientRemoteInterface nick) {
        cliMap.put(namn, nick);
    }

    void create(String namn, ClientRemoteInterface host) {
        this.host = host;
        RegPeer(namn, host);
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
}
