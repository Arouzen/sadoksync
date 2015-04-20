/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class ServerS implements Runnable {

    ServerSocket serverSocket = null;
    Boolean listening;
    ServerMonitor sMonitor;

    public ServerS() {
        sMonitor = new ServerMonitor();

        try {
            serverSocket = new ServerSocket(4444);
            listening = true;
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

    }

    @Override
    public void run() {

        while (listening) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                (new ConnectionHandler(clientSocket, sMonitor)).start();
            } catch (IOException ex) {
                Logger.getLogger(ServerS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
