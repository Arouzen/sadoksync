/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;
import com.sadoksync.msg.RegistryRemoteInterface;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Pontus
 */
public class ClientInterface  extends UnicastRemoteObject implements ClientRemoteInterface{
    String nick;
    Comunity com;
    public ClientInterface(String nick, Comunity com) throws RemoteException, MalformedURLException {
        this.com = com;
        this.nick = nick;
    }

    @Override
    public void register(String name, ClientRemoteInterface rri) throws RemoteException {
        com.RegPeer(name, rri);
    }

    @Override
    public void ping(ClientRemoteInterface rri) throws RemoteException {
        rri.register(nick, this);
    }

    @Override
    public void setComunity(ClientRemoteInterface rri) throws RemoteException {
        com.setHost(rri);
    }


    
}
