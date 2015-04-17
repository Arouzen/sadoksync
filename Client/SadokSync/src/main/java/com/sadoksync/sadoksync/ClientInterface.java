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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author Pontus
 */
public class ClientInterface extends UnicastRemoteObject implements ClientRemoteInterface {

    String nick;
    Lobby lb;
    Client cli;
    Peer pr;
    
    public ClientInterface(Peer pr) throws RemoteException, MalformedURLException {
        this.pr = pr;
    }

    @Override
    public void register(String name, ClientRemoteInterface cri, String ipAddr) throws RemoteException {
        pr.RegPeer(new PeerReg(name, cri, ipAddr));
        //Respond with myIP
        cri.setStream(pr.getMyIp());
    }

    @Override
    public void ping(ClientRemoteInterface rri) throws RemoteException {
        rri.register(nick, this, pr.getMyIp());
    }

    @Override
    public void setComunity(ClientRemoteInterface rri) throws RemoteException {
        pr.setComunityHost(rri);
        //Show the client
        //pr.openClient();

    }

    @Override
    public void setComunityList(List nameli) throws RemoteException {
        //Display nameli in list of comunitys
        DefaultListModel lm = new DefaultListModel();

        for (Object s : nameli) {
            lm.addElement((String) s);
        }

        final DefaultListModel flm = lm;

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                lb.jList1.setModel(flm);
            }
        });

    }

    public void setLobby(Lobby lb) {
        this.lb = lb;
    }

    void setClient(Client cli) {
        this.cli = cli;
    }
    
    void setPeer(Peer pr){
        this.pr = pr;
    }

    @Override
    public void setStream(String myIp) throws RemoteException {
        pr.setMedia(myIp);
    }

}
