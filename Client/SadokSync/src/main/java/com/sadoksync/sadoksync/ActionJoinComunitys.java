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
class ActionJoinComunitys implements Runnable {
    Peer pr;
    String addr;
    String uuid;
    boolean host;
    public ActionJoinComunitys(Peer pr, String addr, String uuid, boolean host) {
        this.pr = pr;
        this.addr = addr;
        this.uuid = uuid;
        this.host = host;
    }
    
    

    @Override
    public void run() {
       pr.joinComunity(uuid, host);
    }
}
