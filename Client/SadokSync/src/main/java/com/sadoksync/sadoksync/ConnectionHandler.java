package com.sadoksync.sadoksync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.ArrayList;
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
                    /*
                     case "your ip":
                     //Get name should be a uuid
                     System.out.println("My IP is: " + msg.getipAddr());
                     pr.setMyIP(msg.getipAddr());
                     break;
                     */
                    case "Set Playlist":
                        System.out.println("Message: Set Playlist");
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            PublicPlaylist pl = new PublicPlaylist(pr, msg.getList());
                            pr.getClient().setPlayList(pl);
                        }

                        break;

                    case "Playlist":
                        System.out.println("Message: Playlist");
                        if (pr.com.getUUID().equals(msg.getUUID())) {

                            if (msg.getText().equals("add") && pr.isHost()) {
                                System.out.println("Message: Playlist: Adding: ");
                                pr.getClient().addtoPlaylist(msg.getPair());

                                //pr.DeliverPlaylistToComunity();
                            } else if (msg.getText().equals("add") && !pr.isHost()) {
                                //Relay message
                            }
                        }

                        break;
                    case "Get number of Peers":
                        if (pr.com.getUUID() != null) {
                            if (pr.com.getUUID().equals(msg.getUUID())) {
                                Message retmsg = new Message();
                                retmsg.setType("Comunity Size");
                                retmsg.setText(Integer.toString(pr.com.getNrOfPeers()));
                                pr.sendMsg(msg.getipAddr(), 3333, retmsg);
                            } else {
                                //Weak solution
                                Message retmsg = new Message();
                                retmsg.setType("Comunity is Dead");
                                retmsg.setText(msg.getUUID());
                                pr.sendMsg(msg.getipAddr(), 3333, retmsg);
                            }
                        } else {
                            //Weak solution
                            Message retmsg = new Message();
                            retmsg.setType("Comunity is Dead");
                            retmsg.setText(msg.getUUID());
                            pr.sendMsg(msg.getipAddr(), 3333, retmsg);
                        }

                        break;

                    case "Ping":
                        System.out.println("Recived Ping");
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            pr.Pong(msg);
                        }

                        break;
                    case "Pong":
                        System.out.println("Reciving Pong");
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            pr.handlePong(msg);
                        }

                        break;
                    case "Set Stream":
                        System.out.println("Set Stream");
                        String comUUID = pr.com.getUUID();
                        String msgUUID = msg.getUUID();
                        if (comUUID.equals(msgUUID)) {
                            //pr.com.setHost(msg.getipAddr());
                            pr.getClient().setHost(msg.getipAddr());
                            pr.getClient().setPort("1024");
                            pr.getClient().setRtspPath(msg.getName());
                            pr.getClient().setMediaType(msg.getText());
                            pr.getClient().connectToRtsp();
                        }

                        break;

                    case "Set First Stream":
                        System.out.println("Set First Stream");

                        //pr.com.setHost(msg.getipAddr());
                        pr.getClient().setHost(msg.getipAddr());
                        pr.getClient().setPort("1024");
                        pr.getClient().setRtspPath(msg.getName());
                        pr.getClient().setMediaType(msg.getText());
                        pr.getClient().connectToRtsp();

                        break;

                    case "Set Host":
                        //Should be done regardles off UUID
                        System.out.println("Setting comunity name: " + msg.getName());
                        pr.setComunityName(msg.getName());
                        System.out.println("Setting host: " + msg.getipAddr());
                        pr.setHost(msg.getipAddr(), msg.getUUID());
                        break;
                    case "Register Client":
                        System.out.println("Register Client: " + msg.getName());
                        System.out.println("MESSAGE UUID: " + msg.getUUID());
                        System.out.println("COMMUNIRT UUID: " + pr.com.getUUID());
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            pr.PeerToJoin(msg);
                        }
                        break;
                    case "Join Comunity":
                        //Should be done regardles off UUID
                        System.out.println("Join Comunity");

                        if (pr.isHost()) {
                            pr.PeerToJoin(msg);
                        } else {
                            pr.sendToHost(msg);
                        }

                        //If comunity Host register the peer
                        //If not comunity Host, send this to comunity Host
                        break;
                    case "Comunity List":
                        //Should be done regardles off UUID
                        System.out.println("Comunity List");
                        List<ComunityRegistration> li = (List<ComunityRegistration>) msg.getList();

                        DefaultListModel lm = new DefaultListModel();
                        pr.getLobby().ali = new ArrayList();
                        ComunityRegistration cm;
                        for (Object s : li) {
                            cm = (ComunityRegistration) s;
                            pr.addKnownComunity(cm);
                            lm.addElement(cm.getName());
                            pr.getLobby().ali.add(cm.getUUID());
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
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            System.out.println("Reciving chat message " + msg.getText());
                            pr.getClient().addToChatOutput(msg.getText());
                        }

                        break;
                    case "removefromlist":
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            System.out.println("Reciving removefromlist message: " + msg.getName());
                            pr.getClient().getPublicPlaylist().removefrommyPlaylist(msg.getName());
                        }

                        break;
                    case "removePeerbyNick":
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            System.out.println("Reciving removePeerbyNick message: " + msg.getName());
                            pr.removePeerbyNick(msg.getName());
                        }

                        break;
                    case "removePeerFromCommunity":
                        if (pr.com.getUUID().equals(msg.getUUID())) {
                            System.out.println("Reciving removePeerFromCommunity message");
                            pr.removePeerFromCommunity(msg.getName());
                        }

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
