/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

/**
 *
 * @author Pontus
 */
public class ActionApplyProperties implements Runnable {
    Peer pr; 
    String vlcpath;
    String ipAddr;
    public ActionApplyProperties(Peer pr, String vlcpath, String ipAddr) {
        this.pr = pr;
        this.vlcpath = vlcpath;
        this.ipAddr = ipAddr;
    }
    
    @Override
    public void run() {
        pr.setMyIP(ipAddr);
        pr.setMyVlcPath(vlcpath);
        pr.openLobby();
        
        pr.setMySR(new ServiceRegistry("Sadoksync", ipAddr));
    }
    
}
