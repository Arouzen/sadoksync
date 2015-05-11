/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Pontus
 */
class ServiceRegistryConnectionHandler extends Thread {

    SynchReg synchMap;
    Socket clientSocket;
    Peer pr;
    ObjectInputStream in;
    Map<String, ComunityRegistration> cMap;

    //BufferedOutputStream out;
    public ServiceRegistryConnectionHandler(Socket clientSocket, Map<String, ComunityRegistration> cMap, Peer pr) {
        this.cMap = cMap;
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
                    case "Register Client":
                        pr.getDebugSys().println("Register Client: Error Wrong Handler");
                        //System.out.println("Register Client: Error Wrong Handler");
                        break;
                    case "deRegister":
                        //Get name should be a uuid
                        cMap.remove(msg.getUUID());
                        break;
                    case "Comunity is Dead":
                        //Get name should be a uuid
                        cMap.remove(msg.getUUID());
                        break;
                    case "Comunity Size":
                        //Get name should be a uuid
                        cMap.get(msg.getUUID()).setSize(msg.getText());
                        break;

                    case "Find All":
                        pr.getDebugSys().println("Find All");
                        //System.out.println("Find All");
                        /*
                         msg = new Message();
                         String cip = clientSocket.getInetAddress().toString();
                         msg.setType("your ip");
                         msg.setipAddr(cip);
                         pr.sendMsg(cip, 40, msg);
                         */
                        //Turn Map into List
                        List retList = new LinkedList();
                        Set s = cMap.keySet(); // Needn't be in synchronized block
                        ComunityRegistration opr;
                        synchronized (cMap) {  // Synchronizing on m, not s!
                            Iterator i = s.iterator(); // Must be in synchronized block
                            while (i.hasNext()) {
                                opr = cMap.get(i.next());

                                //Add to list
                                retList.add(opr);
                            }
                        }
                        String retIP = msg.getipAddr();
                        //Create return message
                        msg = new Message();
                        msg.setType("Comunity List");
                        msg.setList(retList);
                        pr.sendMsg(retIP, 40, msg);
                        break;
                    case "Comunity Registration":
                        pr.getDebugSys().println("Comunity Registration: " + msg.getName() + " @" + msg.getipAddr());
                        //System.out.println("Comunity Registration: " + msg.getName() + " @" + msg.getipAddr());
                        cMap.put(msg.getUUID(), new ComunityRegistration(msg.getName(), msg.getipAddr(), msg.getText(), msg.getUUID()));
                        break;
                }
            }

            in.close();
            clientSocket.close();

        } catch (ClassNotFoundException cnfe) {
            pr.getDebugSys().println(cnfe.toString());
            //System.out.println(cnfe.toString());
            return;
        } catch (OptionalDataException ode) {
            pr.getDebugSys().println(ode.toString());
            //System.out.println(ode.toString());
            return;
        } catch (IOException ioe) {
            pr.getDebugSys().println(ioe.toString());
            //System.out.println(ioe.toString());
            return;
        }

    }

}
