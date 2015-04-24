package com.sadoksync.sadoksync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author Pontus
 */
public class ConnectionHandler extends Thread {

    SynchReg synchMap;
    Socket clientSocket;
    Peer pr;
    ObjectInputStream in;
    //BufferedOutputStream out;

    ConnectionHandler(Socket clientSocket, Peer pr) {
        this.clientSocket = clientSocket;
        this.pr = pr;
        //this.synchMap = pr.getSynchReg();
    }

    @Override
    public void run() {

        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            //out = new BufferedOutputStream(clientSocket.getOutputStream());

            //Read Object
            Object msgObj;

            msgObj = in.readObject();

            if (msgObj instanceof Message) {
                Message msg = ((Message) msgObj);
                switch (msg.getType()) {

                    case "Set Playlist":
                        System.out.println("Message: Set Playlist");
                        PublicPlaylist pl = new PublicPlaylist(pr, msg.getList());
                        pr.getClient().setPlayList(pl);
                        pr.getClient().updateRightPanel(pr.getClient().getPlaylist());
                        break;
                    case "Playlist":
                        System.out.println("Message: Playlist");
                        if (msg.getText().equals("add") && pr.isHost()) {
                            System.out.println("Message: Playlist: Adding: ");

                            //Add to playlist and then send the play list to all.
                            pr.getClient().addtoPlaylist(msg.getPair());
                            pr.DeliverPlaylistToComunity();
                        } else if (msg.getText().equals("add") && !pr.isHost()) {
                            //Relay message
                        }
                        break;
                    case "Ping":
                        pr.Pong(msg);
                        break;
                    case "Pong":
                        pr.handlePong(msg);
                        break;
                    case "Set Stream":
                        System.out.println("Set Stream");
                        //pr.com.setHost(msg.getipAddr());
                        pr.getClient().setHost(msg.getipAddr());
                        pr.getClient().setPort("5555");
                        pr.getClient().setRtspPath(msg.getName());
                        pr.getClient().connectToRtsp();
                        break;
                    case "Set Host":
                        System.out.println("Setting comunity name");
                        pr.setComunityName(msg.getName());
                        System.out.println("Setting host");
                        pr.setHost(msg.getipAddr());
                        break;
                    case "Register Client":
                        System.out.println("Register Client: " + msg.getName());
                        pr.PeerToJoin(msg);
                        break;
                    case "Join Comunity":
                        System.out.println("Join Comunity");
                        pr.PeerToJoin(msg);
                        //If comunity Host register the peer
                        //If not comunity Host, send this to comunity Host
                        break;
                    case "Comunity List":
                        System.out.println("Comunity List");
                        List<ComunityRegistration> li = (List<ComunityRegistration>) msg.getList();

                        DefaultListModel lm = new DefaultListModel();
                        ComunityRegistration cm;
                        for (Object s : li) {
                            cm = (ComunityRegistration) s;
                            pr.addKnownComunity(cm);
                            lm.addElement(cm.getName());
                        }

                        final DefaultListModel flm = lm;

                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                pr.getLobby().jList1.setModel(flm);
                            }
                        });
                        break;
                    case "Find All":
                        System.out.println("Find All: ERROR. Wrong handler");
                        break;
                    case "Comunity Registration":
                        System.out.println("Comunity Registration: ERROR. Wrong handler");
                        break;
                    case "chat message":
                        pr.getClient().addToChatOutput(msg.getText());
                        break;
                }
            }

            in.close();
            clientSocket.close();

        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.toString());
            return;
        } catch (OptionalDataException ode) {
            System.out.println(ode.toString());
            return;
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
            return;
        }

    }

}
