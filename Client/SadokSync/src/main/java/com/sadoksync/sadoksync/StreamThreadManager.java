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
    Client cli;

    StreamThread st;
    boolean stream;

    public StreamThreadManager() {

    }

    public void startStream(PublicPlaylist playlist, Client cli) {
        this.playlist = playlist;
        this.cli = cli;

        System.out.println("StreamThreadManager: startStream");

        //Kill any old stream
        killStream();

        System.out.println("Starting stream");
        st = new StreamThread(playlist, cli);

        st.start();
    }

    public void killStream() {
        System.out.println("StreamThreadManager: killStream");
        //Make sure that there is a stream to kill.
        if (st != null) {
            //if (st.isAlive()) {
            System.out.println("Killing stream");
            st.kill();
            try {
                //Wait for the st thread to die.
                st.join();
                System.out.println("Thread joined");
            } catch (InterruptedException ex) {
                Logger.getLogger(StreamThreadManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            //}
        }
    }

    void stop() {
        //st.stopMedia();
        killStream();
        playlist.removeFirstInQueue();
        cli.setMode("playlist");
        cli.startStream();
    }
}
