/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class ClientC implements Runnable {

    Socket clientSocket = null;
    BufferedInputStream in = null;
    BufferedOutputStream out = null;

    public ClientC() {
        try {

            clientSocket = new Socket("127.0.0.1", 4444);

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + "127.0.0.1" + ".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: " + "127.0.0.1" + "");
            System.exit(1);
        }
    }

    @Override
    public void run() {

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());

            //Create and send the message
            byte[] toServer = "My message".getBytes(); // convert to byte array
            out.write(toServer, 0, toServer.length); // send message
            out.flush();                       // ensure that message is sent

            //Asnchronous message passing... Response is sent to a ServerSocket
            
            out.close();           // close output stream
            in.close();            // close input stream
            clientSocket.close();  // close connection

        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(1);
        }

    }

}
