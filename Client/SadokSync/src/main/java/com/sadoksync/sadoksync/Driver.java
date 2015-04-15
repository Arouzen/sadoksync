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
    public static void main(String[] args) {
        new Driver();
    }

    public Driver() {
        new ServiceRegistry("Sadoksync");
        String nick = "ME";
        
        pr = new Peer();
        pr.setNick(nick);
        
        //pr.createComunity("ComunityName", "Stuff");
        //pr.registerComunity("localhost","Sadoksync",1099);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                lb = new Lobby(pr);
                pr.setLobby(lb);
                lb.setVisible(true);
            }
        });
        
        
        /*
        
        
        pr = new Peer();
        
        
        pr.findAllComunity("localhost","Sadoksync",1099);
*/
/*
        ClientRemoteInterface cri;
        try {
            cri = new ClientInterface(name);
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }
}
