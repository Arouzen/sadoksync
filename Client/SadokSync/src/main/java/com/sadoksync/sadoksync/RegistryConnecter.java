/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;
import com.sadoksync.msg.RegistryRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class RegistryConnecter {

    RegistryRemoteInterface rri;
    String name;
    String host;
    int port;

    public RegistryConnecter(String host) {
        this.host = "rmi://" + host;
        this.name = "Sadoksync";
        this.port = 1099;
    }

    public RegistryConnecter(String host, String name) {
        this.host = "rmi://" + host;
        this.name = name;
        this.port = 1099;
    }

    public RegistryConnecter(String host, String name, int port) {
        this.host = "rmi://" + host;
        this.name = name;
        this.port = port;
    }

    public boolean Connect() {
        System.out.println("RegistryConnecter: Connect");
        //Locate or Create a rmi registry. Default on port 1099
        try {
            LocateRegistry.getRegistry(this.host, this.port).list();
        } catch (RemoteException e) {
            //LocateRegistry.createRegistry(1099);
        }

        //Lockup service in RMI Registry
        try {
            rri = (RegistryRemoteInterface) Naming.lookup(this.name);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(RegistryConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    void register(String name, ClientRemoteInterface cri, String topic) {
        System.out.println("RegistryConnecter: register");
        try {
            rri.register(name, cri, topic);
        } catch (java.rmi.RemoteException re) {
            re.printStackTrace();
        }
    }

    void getComunity(String name, ClientRemoteInterface cri) {
        System.out.println("RegistryConnecter: getComunity");
        try {
            rri.getComunity(name, cri);
        } catch (java.rmi.RemoteException re) {
            re.printStackTrace();
        }
    }

    void getAll(ClientRemoteInterface cri) {
        System.out.println("RegistryConnecter: getAll");
          try {
            rri.getAllComunitys(cri);
        } catch (java.rmi.RemoteException re) {
            re.printStackTrace();
        }
    }

}
