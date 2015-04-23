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
    
    public ActionCreateComunity(Peer pr, String addr, String cname, String topic){
        this.pr = pr;
        this.addr = addr;
        this.cname = cname;
        this.topic = topic;
    }
    
    @Override
    public void run() {
        System.out.println("ActionCreateComunity: Creating Comunity " + cname);
        pr.createComunity(cname, topic);
        
        System.out.println("ActionCreateComunity: Registring Comunity " + cname);
        pr.registerComunity(addr,4444);

    }
}
    

