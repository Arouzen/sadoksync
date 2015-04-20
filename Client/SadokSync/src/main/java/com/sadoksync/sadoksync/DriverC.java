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
public class DriverC {
    ClientC cli;
    Thread cliThread;
    public static void main(String[] args){
        new DriverC();
    }
    
    public DriverC(){
        cli = new ClientC();
        cliThread = new Thread(cli);
        cliThread.start();
    }
}
