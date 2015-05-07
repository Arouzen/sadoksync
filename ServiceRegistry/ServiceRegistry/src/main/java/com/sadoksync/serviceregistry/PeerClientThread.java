/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.serviceregistry;

import com.sadoksync.message.RegistryMessage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Pontus
 */
class PeerClientThread extends Thread {

    Socket clientSocket = null;
    ObjectOutputStream out = null;
    RegistryMessage msg;
    Boolean connected;
    String host;
    int port;

    PeerClientThread(String host, int port, RegistryMessage msg) {
        this.msg = msg;
        this.host = host;
        this.port = port;
        System.err.println("Initializing msg sender: " + msg.getType());

    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(host, port);
            connected = true;
        } catch (UnknownHostException e) {
            connected = false;
        } catch (IOException e) {
            connected = false;
        }
        System.err.println("msg sender: Sender " + msg.getType());

        if (connected) {
            try {
                //in = new BufferedInputStream(clientSocket.getInputStream());
                out = new ObjectOutputStream(clientSocket.getOutputStream());

                //Create and send the message
                //byte[] toServer = msg.getBytes(); // convert to byte array
                //out.write(toServer, 0, toServer.length); // send message
                out.writeObject(msg);
                out.flush();                       // ensure that message is sent

                //Asnchronous message passing... Response is sent to a ServerSocket
                out.close();           // close output stream
                //in.close();            // close input stream
                clientSocket.close();  // close connection

            } catch (IOException e) {
                System.out.println(e.toString());
                System.out.println("Could not send " + msg.getType() + " to " + clientSocket.getInetAddress().toString());
                //System.exit(1);
            }

        }

    }
}
