/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.serviceregistry;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author Arouz
 */
public class ServiceRegistry {
    SynchReg criMap;
    private static final int REGISTRY_PORT_NUMBER = 1099;

    public ServiceRegistry() {
        criMap = new SynchReg();
        
        try {
            try {
                LocateRegistry.getRegistry(REGISTRY_PORT_NUMBER).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(REGISTRY_PORT_NUMBER);
            }
            Naming.rebind("rmi://localhost/Sadoksync", new RegistryImplementation(criMap));
        } catch (RemoteException | MalformedURLException re) {
            System.out.println(re);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new ServiceRegistry();

    }
}
