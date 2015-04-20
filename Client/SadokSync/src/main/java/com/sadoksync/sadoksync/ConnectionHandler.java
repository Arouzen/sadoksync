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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class ConnectionHandler extends Thread {

    Socket clientSocket;
    Peer pr;
    BufferedInputStream in;
    BufferedOutputStream out;

    ConnectionHandler(Socket clientSocket, Peer pr) {
        this.clientSocket = clientSocket;
        this.pr = pr;
    }

    @Override
    public void run() {

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());

            //Read Message
            byte[] msg = new byte[4096];
            int bytesRead = 0;
            int n;

            while ((n = in.read(msg, bytesRead, 256)) != -1) {
                bytesRead += n;
                if (bytesRead == 4096) {
                    break;
                }
                if (in.available() == 0) {
                    break;
                }
            }

            //Handle Message
            System.out.println(new String(msg));

            //Close connection
            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }

    }

}
