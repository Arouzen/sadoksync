/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Pontus
 */
public class ConnectionHandler extends Thread {

    SynchReg synchMap;
    Socket clientSocket;
    Peer pr;
    ObjectInputStream in;
    //BufferedOutputStream out;

    ConnectionHandler(Socket clientSocket, Peer pr) {
        this.clientSocket = clientSocket;
        this.pr = pr;
        //this.synchMap = pr.getSynchReg();
    }

    @Override
    public void run() {

        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            //out = new BufferedOutputStream(clientSocket.getOutputStream());

            //Read Object
            Object msgObj;

            msgObj = in.readObject();

            if (msgObj instanceof Message) {
                Message msg = ((Message) msgObj);
                switch (msg.getType()) {
                    case "Set Stream":
                        System.out.println("Set Stream");
                        pr.getClient().setHost(msg.getipAddr());
                        pr.getClient().setPort("5555");
                        pr.getClient().setRtspPath(msg.getName());
                        
                        break;
                    case "Register Client":
                        System.out.println("Register Client");

                        break;
                    case "Join Comunity":
                        System.out.println("Join Comunity");
                        pr.PeerToJoin(msg);
                        //If comunity Host register the peer
                        //If not comunity Host, send this to comunity Host
                        break;

                    case "Comunity List":
                        System.out.println("Comunity List");
                        List<ComunityRegistration> li = (List<ComunityRegistration>) msg.getList();

                        DefaultListModel lm = new DefaultListModel();
                        ComunityRegistration cm;
                        for (Object s : li) {
                            cm =(ComunityRegistration) s; 
                            pr.addKnownComunity(cm);
                            lm.addElement(cm.getName());
                        }

                        final DefaultListModel flm = lm;

                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                pr.getLobby().jList1.setModel(flm);
                            }
                        });

                        break;

                    case "Find All":
                        System.out.println("Find All: ERROR. Wrong handler");
                        break;
                    case "Comunity Registration":
                        System.out.println("Comunity Registration: ERROR. Wrong handler");

                        break;

                }
            }

            in.close();
            clientSocket.close();

        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.toString());
            return;
        } catch (OptionalDataException ode) {
            System.out.println(ode.toString());
            return;
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
            return;
        }

    }

}
