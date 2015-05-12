/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
class ServiceRegistryUppdater extends Thread {

    Map<String, ComunityRegistration> cMap;
    Peer pr;

    public ServiceRegistryUppdater(Peer pr, Map<String, ComunityRegistration> cMap) {
        this.cMap = cMap;
        this.pr = pr;
    }

    @Override
    public void run() {
        while (true) {
            //loop throug cMap.
            System.out.println("Pinnging Comunitys");
            String key;
            ComunityRegistration opr;
            Set s = cMap.keySet();
            Iterator i = s.iterator();
            
            //For each contact and ask for number of peers
            while (i.hasNext()) {
                key = (String) i.next();
                opr = (ComunityRegistration) cMap.get(key);

                Message msg = new Message();
                msg.setipAddr(pr.getMyIp());
                msg.setUUID(opr.getUUID());
                msg.setType("Get number of Peers");
                this.sendMsg(opr.getHost(), 40, msg, key);

            }
            
            try {
                //then sleep 50sec
                Thread.sleep(50000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServiceRegistryUppdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void sendMsg(String host, int port, Message msg, String key) {
        Socket clientSocket;
        boolean connected;
        ObjectOutputStream out;

        try {
            clientSocket = new Socket(host, port);
            connected = true;

            if (connected) {
                try {
                    out = new ObjectOutputStream(clientSocket.getOutputStream());

                    out.writeObject(msg);
                    out.flush();                       // ensure that message is sent

                    out.close();           // close output stream

                    clientSocket.close();  // close connection

                } catch (IOException e) {
                    pr.getDebugSys().println(e.toString());
                //System.out.println(e.toString());

                    pr.getDebugSys().println("Could not send " + msg.getType() + " to " + clientSocket.getInetAddress().toString());
                }

            }

        } catch (UnknownHostException e) {
            pr.getDebugSys().println("Don't know about host: " + host + ".");
            //System.out.println("Don't know about host: " + host + ".");

            pr.getDebugSys().println(e.getMessage());
            //System.out.println(e.getMessage());
            //System.exit(1);
            connected = false;
        } catch (IOException e) {
            pr.getDebugSys().println("Couldn't get I/O for " + "the connection to: " + host + "");
            pr.getDebugSys().println(e.getMessage());

            cMap.remove(key);

            connected = false;
        }
    }
}
