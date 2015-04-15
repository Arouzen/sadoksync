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
public interface RegistryRemoteInterface extends Remote {
    void register(String name, ClientRemoteInterface rri, String topic) throws RemoteException;
    void getComunity(String name, ClientRemoteInterface cri) throws RemoteException;
    void getAllComunitys(ClientRemoteInterface cri) throws RemoteException;
}
