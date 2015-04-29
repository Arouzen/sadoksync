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

    public static void main(String[] args) {
        new Driver();
    }

    public Driver() {

        pr = new Peer();
        pr.run();        
        
    }
}
