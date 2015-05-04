/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pontus
 */
public class StreamThreadManager {

    PublicPlaylist playlist;
    Client client;

    StreamThread st;
    boolean stream;

    public StreamThreadManager(PublicPlaylist playlist, Client client) {
        this.playlist = playlist;
        this.client = client;
    }

    public void startStream() {
        if (st != null) {
            st = new StreamThread(playlist, client);
        } else {
            killStream();
            st = new StreamThread(playlist, client);
        }
    }
    
    public void killStream() {
        st.kill();
        try {
            //Wait for the st thread to die.
            st.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(StreamThreadManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
