/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

/**
 *
 * @author Pontus
 */
public class ActionSetName implements Runnable {

    Peer pr;
    String nick;
    Lobby lb;

    public ActionSetName(Peer pr, String nick, Lobby lb) {
        this.pr = pr;
        this.nick = nick;
        this.lb = lb;
    }

    @Override
    public void run() {
        System.out.println("Nick set t: " + nick);
        pr.setNick(nick);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                lb.jButton3.setEnabled(true);
                lb.jButton4.setEnabled(false);
            }
        });

    }
}
