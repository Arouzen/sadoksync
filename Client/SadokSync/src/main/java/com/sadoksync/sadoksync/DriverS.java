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
public class DriverS {
    ServerS s;
    Thread sThread;
    public static void main(String[] args){
        DriverS d = new DriverS();
    }
    
    public DriverS(){
        s = new ServerS();
        sThread = new Thread(s);
        sThread.start();
    }
}
