/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class Driver {

 
    Peer pr;
    Lobby lb;
    Client cli;
    public static void main(String[] args) {
        new Driver();
    }

    public Driver() {
        new ServiceRegistry("Sadoksync");     
        pr = new Peer();
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                /*
                cli = new Client();
                cli.setVisible(false);
                pr.setClient(cli);
                */
                
                lb = new Lobby(pr/*, cli*/);
                lb.setVisible(true);
                pr.setLobby(lb);    
            }
        });
    }
}
