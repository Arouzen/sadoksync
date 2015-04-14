/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.msg;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Pontus
 */
public interface ClientRemoteInterface extends Remote {
    //Registrera en client och des nick med ett comunity
    void register(String nick, ClientRemoteInterface rri) throws RemoteException;
    void ping(ClientRemoteInterface rri) throws RemoteException;
    void setComunity(ClientRemoteInterface rri) throws RemoteException;
}
