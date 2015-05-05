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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Starting stream");
        st = new StreamThread(playlist, cli);

        st.start();
    }

    public void killStream() {
        System.out.println("StreamThreadManager: killStream");
        //Make sure that there is a stream to kill.
        if (st != null) {
            System.out.println("st not null");
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
        }else{
            System.out.println("st is null");
        }
    }

    boolean isAlive(){
        boolean ret = false;
        if (st != null) {
            ret =  st.isAlive();  
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
