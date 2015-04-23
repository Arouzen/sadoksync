/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;


import java.io.Serializable;

/**
 *
 * @author Pontus
 */
public class PeerReg implements Serializable {
    String nick;
    String ipAddr;
    public PeerReg(String nick, String ipAddr) {
        this.nick = nick;
        this.ipAddr = ipAddr;
    }
    public String getNick(){
        return nick;
    }
    public String getAddr(){
        return ipAddr;
    }
}
