/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;


import com.sadoksync.msg.ClientRemoteInterface;
import com.sadoksync.msg.RegistryRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
    public void register(String name, ClientRemoteInterface cri, String topic) throws RemoteException {
        //System.out.println("register: " + name + ", " + topic);
        //name är något som en användare kan använda för att identifiera ett comunity.
        //rri är en contakt information till den nuvarande hosten. 
        
        //Lagra host och rri i en synchronizerad hashmap.
        criMap.put(name, new ComunityRegistration(name, cri, topic));
    }

    @Override
    public void getComunity(String name, ClientRemoteInterface cri) throws RemoteException {
        //ystem.out.println("getComunity: " + name);
        //Responds with a connection to the host of a community with the name name.
        try {
            cri.setComunity(criMap.get(name).getHost()); // Tells the consumer we are done.
        } catch (java.rmi.RemoteException re) {
            re.printStackTrace();
        }
        
    }

    @Override
    public void getAllComunitys(ClientRemoteInterface cri) throws RemoteException {
        //System.out.println("getAllComunity");
        
        List nameli;
        nameli = criMap.getKeyList();
                
        try {
            cri.setComunityList(nameli);
        } catch (java.rmi.RemoteException re) {
            re.printStackTrace();
        }
    }

}
