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
public class ActionCreateComunity implements Runnable {
    Peer pr; 
    String addr; 
    String cname;
    String topic; 
    String nick;
    
    public ActionCreateComunity(Peer pr, String addr, String cname, String topic, String nick){
        this.pr = pr;
        this.addr = addr;
        this.cname = cname;
        this.topic = topic;
        this.nick = nick;
    }
    
    @Override
    public void run() {
        System.out.println("ActionCreateComunity: Creating Comunity " + cname);
        pr.createComunity(cname, topic);
        
        System.out.println("ActionCreateComunity: Registring Comunity " + cname);
        pr.registerComunity(addr,4444);
        pr.setHost(true);
        pr.joinComunity(cname, addr, nick);
    }
}
    

