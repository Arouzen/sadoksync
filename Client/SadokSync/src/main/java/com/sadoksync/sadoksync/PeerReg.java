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
public class PeerReg {
    String name;
    ClientRemoteInterface addr;
    public PeerReg(String name, ClientRemoteInterface host) {
        this.name = name;
        this.addr = host;
    }
    public String getName(){
        return name;
    }
    public ClientRemoteInterface getAddr(){
        return addr;
    }
}
