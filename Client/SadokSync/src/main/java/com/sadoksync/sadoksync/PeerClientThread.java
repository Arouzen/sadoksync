/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

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
    Message msg;
    Boolean connected;

    PeerClientThread(String host, int port, Message msg) {
        this.msg = msg;

        try {

            clientSocket = new Socket(host, port);
            connected = true;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host + ".");
            //System.exit(1);
            connected = false;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: " + host + "");
            //System.exit(1);
            connected = false;
        }
    }

    @Override
    public void run() {
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
