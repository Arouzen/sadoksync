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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        this.synchMap = pr.getSynchReg();
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
                    case "Register Client":
                        System.out.println("Register Client");
                        break;
                    case "Join Comunity":
                        System.out.println("Join Comunity");
                        break;
                    case "Find All":
                        System.out.println("Find All");
                        break;
                    case "Comunity Registration":
                        System.out.println("Comunity Registration");
                        synchMap.put(msg.getName(), new ComunityRegistration(msg.getName(), msg.getipAddr(), msg.getText()));
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
