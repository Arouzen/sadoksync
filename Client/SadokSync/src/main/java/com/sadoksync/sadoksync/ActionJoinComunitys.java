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
    String cname;
    String nick;
    public ActionJoinComunitys(Peer pr, String addr, String cname, String nick) {
        this.pr = pr;
        this.addr = addr;
        this.cname = cname;
        this.nick = nick;
    }
    
    

    @Override
    public void run() {
        //Need rework here because of hardcoded lobby stuff
       pr.com.setRegistryAddr(addr);
       pr.joinComunity(cname, addr, nick);
    }
}
