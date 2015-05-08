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

    DebugSys dbs;
    PublicPlaylist playlist;
    Client cli;

    StreamThread st;
    boolean stream;

    public StreamThreadManager(DebugSys dbs) {
        this.dbs = dbs;
    }

    public void startStream(PublicPlaylist playlist, Client cli) {
        this.playlist = playlist;
        this.cli = cli;
        dbs.println("StreamThreadManager: startStream");
        //System.out.println("StreamThreadManager: startStream");

        //Kill any old stream
        killStream();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        dbs.println("Starting stream");
        //System.out.println("Starting stream");
        st = new StreamThread(playlist, cli, dbs);

        st.start();
    }

    public void killStream() {
        dbs.println("StreamThreadManager: killStream");
        //System.out.println("StreamThreadManager: killStream");
        //Make sure that there is a stream to kill.
        if (st != null) {
            dbs.println("st not null");
            //System.out.println("st not null");
            //if (st.isAlive()) {
            dbs.println("Killing stream");
            //System.out.println("Killing stream");
            st.kill();
            try {
                //Wait for the st thread to die.
                st.join();
                dbs.println("Thread joined");
                //System.out.println("Thread joined");
            } catch (InterruptedException ex) {
                Logger.getLogger(StreamThreadManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            //}
        } else {
            dbs.println("st is null");
            //System.out.println("st is null");
        }
    }

    boolean isAlive() {
        boolean ret = false;
        if (st != null) {
            ret = st.isAlive();
        }
        return ret;
    }

    void stop() {
        //st.stopMedia();
        //this.killStream();
        //playlist.removeFirstInQueue();
        //cli.setMode("playlist");
        //cli.startStream();

        st.endMedia();

    }
}
