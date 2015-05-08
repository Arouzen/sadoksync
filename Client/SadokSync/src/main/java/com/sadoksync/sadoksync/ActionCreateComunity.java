/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.util.logging.Level;
import java.util.logging.Logger;

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
        pr.getDebugSys().println("ActionCreateComunity: Creating Comunity " + cname);
        //System.out.println("ActionCreateComunity: Creating Comunity " + cname);
        pr.createComunity(cname, topic);
                
        pr.getDebugSys().println("ActionCreateComunity: Registring Comunity " + cname);
        //System.out.println("ActionCreateComunity: Registring Comunity " + cname);
        pr.registerComunity(addr,3333);

    }
}
    

