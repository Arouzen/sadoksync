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
class PeerServerThread extends Thread {

    ServerSocket serverSocket = null;
    Boolean listening;
    
    Peer pr;
    
    
    public PeerServerThread(Peer pr) {
        this.pr = pr;
        try {
            serverSocket = new ServerSocket(4444);
            listening = true;
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            //System.exit(1);
        }

    }

    @Override
    public void run() {
        System.out.println("Starting Server Socket Thread");
        while (listening) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                (new ConnectionHandler(clientSocket, pr)).start();
            } catch (IOException ex) {
                Logger.getLogger(PeerServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
