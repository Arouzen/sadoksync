/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.serviceregistry;

import com.sadoksync.msg.ClientRemoteInterface;
import com.sadoksync.msg.RegistryRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Pontus
 */
public class RegistryImplementation extends UnicastRemoteObject implements RegistryRemoteInterface {
    SynchReg criMap;
    public RegistryImplementation(SynchReg criMap) throws RemoteException, MalformedURLException {
        this.criMap = criMap;
    }


    @Override
    public void register(String name, ClientRemoteInterface rri) throws RemoteException {
        //name är något som en användare kan använda för att identifiera ett comunity.
        //rri är en contakt information till den nuvarande hosten. 
        
        //Lagra host och rri i en synchronizerad hashmap.
        criMap.put(name, rri);
        

    }

    @Override
    public void getComunity(String name, ClientRemoteInterface cri) throws RemoteException {
        //Responds with a connection to the host of a community with the name name.
        try {
            cri.setComunity(criMap.get(name)); // Tells the consumer we are done.
        } catch (java.rmi.RemoteException re) {
            re.printStackTrace();
        }
        
    }

}
