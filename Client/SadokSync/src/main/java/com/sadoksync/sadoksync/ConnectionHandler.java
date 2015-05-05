package com.sadoksync.sadoksync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author Alexander
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
                        break;
                    case "Playlist":
                        System.out.println("Message: Playlist");
                        if (msg.getText().equals("add") && pr.isHost()) {
                            System.out.println("Message: Playlist: Adding: ");
                            pr.getClient().addtoPlaylist(msg.getPair());

                            //pr.DeliverPlaylistToComunity();
                        } else if (msg.getText().equals("add") && !pr.isHost()) {
                            //Relay message
                        }
                        break;

                    case "Set Stream":
                        System.out.println("Set Stream");
                        pr.getClient().setHost(clientSocket.getInetAddress().toString().substring(1));
                        pr.getClient().setPort("5555");
                        pr.getClient().setMediaType(msg.getText());
                        pr.getClient().setRtspPath(msg.getName());
                        pr.getClient().connectToRtsp();
                        break;
                    case "Register":
                        System.out.println("Register Client from: " + clientSocket.getInetAddress().toString().substring(1) + " end.");
                        pr.regPeer(new PeerReg(msg.getName(), msg.getText()));

                        //pr.PeerToJoin(msg, clientSocket.getInetAddress().toString().substring(1));
                        break;
                    case "Join Confirmed":
                        System.out.println("Join confirmed from:   " + clientSocket.getInetAddress().toString().substring(1) + " end.");
                        pr.setMyIP(msg.getText());
                        break;

                    case "Join Comunity":
                        if (pr.isHost() && (pr.getMyIp().equals("127.0.0.1")) && !clientSocket.getInetAddress().toString().substring(1).equals("127.0.0.1")) {
                            //No longer alone! :D
                            System.out.println("No longer alone! They see me @" + msg.getText());
                            pr.setMyIP(msg.getText()); //This is not looking good
                            pr.com.removePeer("127.0.0.1");
                            pr.com.setHost(msg.getText());
                            pr.regPeer(new PeerReg(pr.getNick(), msg.getText()));
                        }
                        System.out.println("Join Comunity " + clientSocket.getInetAddress().toString().substring(1) + " end.");
                        pr.peerToJoin(msg, clientSocket.getInetAddress().toString().substring(1));
                        pr.confirmJoin(clientSocket.getInetAddress().toString().substring(1));

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
                    case "Ping":
                        pr.Pong(msg, clientSocket.getInetAddress().toString().substring(1));
                        break;
                    case "Pong":
                        pr.handlePong(msg, clientSocket.getInetAddress().toString().substring(1));
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
