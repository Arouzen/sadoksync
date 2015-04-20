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
public class ActionFindAllComunitys implements Runnable {
    Peer pr; 
    String addr; 
    public ActionFindAllComunitys(Peer pr, String addr){
        this.pr = pr;
        this.addr = addr;
    }
    
    @Override
    public void run() {
        pr.findAllComunity(addr,4444);
    }
}
