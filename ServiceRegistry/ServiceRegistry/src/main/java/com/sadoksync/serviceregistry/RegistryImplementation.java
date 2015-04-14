/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.serviceregistry;

import com.sadoksync.msg.RegistryRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Pontus
 */
public class RegistryImplementation extends UnicastRemoteObject implements RegistryRemoteInterface{

    public RegistryImplementation() throws RemoteException, MalformedURLException {
     
    }
    
}
