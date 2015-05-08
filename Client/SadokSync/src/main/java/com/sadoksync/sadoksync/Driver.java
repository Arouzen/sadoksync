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
public class Driver {

    Peer pr;
    DebugSys dbs;

    public static void main(String[] args) {
        new Driver();
    }

    public Driver() {
        
        dbs = new DebugSys();
        dbs.debug(true);
        pr = new Peer(dbs);
        pr.run();
        

    }
}
