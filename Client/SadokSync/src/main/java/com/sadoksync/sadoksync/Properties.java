/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Pontus
 */
public class Properties extends JFrame {

    Peer pr;
    JLabel jLabelIP;
    JLabel jLabelPath;
    JList jListIP;
    JButton jButtonApply;
    JTextField jTextFieldPath;
    JScrollPane jScrollPaneIP;
    JPanel jp;
    public Properties(Peer pr) {
        this.pr = pr;
        jp = new JPanel();
        initComponents();
    }

    private void initComponents() {
        jp.setLayout(new GridLayout(8, 1));
        jp.add(createVLC());
        jp.add(createIP());
        jp.add(createApply());
        
        setContentPane(jp);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void jButtonApplyActionPerformed(ActionEvent evt) {
        int firstSelIx = jListIP.getSelectedIndex();
        String ipAddr = (String) jListIP.getModel().getElementAt(firstSelIx);
        ActionApplyProperties afac = new ActionApplyProperties(pr, jTextFieldPath.getText(), ipAddr);
        new Thread(afac).start();

    }

    private Component createVLC() {
        JPanel pVLC = new JPanel();
        pVLC.setBorder(
                new TitledBorder(new EtchedBorder(), "VLC"));


        jLabelPath = new javax.swing.JLabel();
        jTextFieldPath = new javax.swing.JTextField(20);
        jTextFieldPath.setText("C:\\Program Files (x86)\\VideoLAN\\VLC");
        
        //jLabelPath.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabelPath.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPath.setText("Set your path to 32 bit vlc: ");
        
        pVLC.add(jLabelPath);
        pVLC.add(jTextFieldPath);
        
        return pVLC;
    }

    private Component createIP() {
        JPanel pIP = new JPanel();
        pIP.setBorder(
                new TitledBorder(new EtchedBorder(), "IP"));


        jLabelIP = new javax.swing.JLabel();
        jListIP = new javax.swing.JList();
        jScrollPaneIP = new javax.swing.JScrollPane();
       //jLabelIP.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabelIP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelIP.setText("Pick your IP to connect to: ");
        //Get the ip of the client;
        List<String> iplist = new LinkedList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    iplist.add(addr.getHostAddress());
                    //System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        final String[] ipArray = iplist.toArray(new String[iplist.size()]);
        jListIP.setModel(new javax.swing.AbstractListModel() {
            String[] strings = ipArray;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPaneIP.setViewportView(jListIP);
        
        pIP.add(jLabelIP);
        //pIP.add(jListIP);
        pIP.add(jScrollPaneIP);
        
        return pIP;
    }

    private Component createApply() {
        JPanel pApply = new JPanel();
        pApply.setBorder(
                new TitledBorder(new EtchedBorder(), "VLC"));
        


        jButtonApply = new javax.swing.JButton();
        jButtonApply.setText("Apply");
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }

        });
        
        pApply.add(jButtonApply);
        return pApply;
    }
}
