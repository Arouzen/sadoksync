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
    public static void main(String[] args) {
        new Driver();
    }

    public Driver() {
        String name = "ME";
        pr = new Peer();
        pr.setNick(name);
        pr.createComunity("ComunityName", "Stuff");
        pr.registerComunity();
        
        pr.findAllComunity();


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
