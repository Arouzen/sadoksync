/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class ServiceRegistry extends Thread {

    Map<String, ComunityRegistration> cMap;
    ServerSocket serverSocket = null;
    Boolean listening;

    Peer pr;

    public ServiceRegistry(Peer pr) {
        this.cMap = Collections.synchronizedMap(new HashMap<String, ComunityRegistration>());
        this.pr = pr;
        try {
            serverSocket = new ServerSocket(3333);
            listening = true;
        } catch (IOException e) {
            System.err.println("Could not listen on port: 3333.");
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
                (new ServiceRegistryConnectionHandler(clientSocket, cMap, pr)).start();
            } catch (IOException ex) {
                Logger.getLogger(ServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
