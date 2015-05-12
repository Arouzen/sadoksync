/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 *
 * @author Pontus
 */
public class ActionExitToLobby implements Runnable {

    Peer pr;
    StreamThreadManager stm;
    EmbeddedMediaPlayer mediaPlayer;

    public ActionExitToLobby(Peer pr, StreamThreadManager stm, EmbeddedMediaPlayer mediaPlayer) {
        this.pr = pr;
        this.stm = stm;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void run() {
        pr.getDebugSys().println("ActionExitToLobby: run()");
        //System.out.println("ActionExitToLobby: run()");
        pr.getClient().getPublicPlaylist().removefromPlaylist(pr.getNick());
        //byt host
        if (pr.isHost()) {
            stm.killStream();
            pr.getDebugSys().println("ActionExitToLobby: if isHost()");
            //System.out.println("ActionExitToLobby: if isHost()");
            pr.removePeerbyNick(pr.getNick());

            if (pr.com.isEmpty()) {
                pr.getDebugSys().println("ActionExitToLobby: if pr.com.isEmpty()");
                //System.out.println("ActionExitToLobby: if pr.com.isEmpty()");
                //deregister from registry

                Message deRegister = new Message();
                deRegister.setType("deRegister");
                deRegister.setName(pr.com.getUUID());

                pr.sendMsg(pr.com.getRegistryAddr(), 3333, deRegister);

            } else {
                pr.getDebugSys().println("ActionExitToLobby: else pr.com.isEmpty()");
                //System.out.println("ActionExitToLobby: else pr.com.isEmpty()");

                Message removeHost = new Message();
                removeHost.setType("removePeerFromCommunity");
                removeHost.setName(pr.getNick());

                pr.sendMsgToComunity(removeHost);

                //Simply to migrate the stream
                if (pr.getClient().isPlaylistEmpty()) {
                    pr.Ping(pr.com.getNextPeerIP(), "Move Host");

                } else {
                    pr.getClient().startStream();
                }

            };

            //pr.SendPMap(pr.getMyIp());
        } else {
            pr.getDebugSys().println("ActionExitToLobby: else isHost()");
            //ystem.out.println("ActionExitToLobby: else isHost()");
            Message msg = new Message();
            msg.setType("removePeerbyNick");
            msg.setName(pr.getNick());
            pr.sendMsg(pr.getHost(), 40, msg);
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        pr.com.clearOldCommunity();
        pr.getClient().clearPlaylist();
        pr.getLobby().clearList();
        pr.getLobby().jButton1.setEnabled(true);
        pr.getLobby().jTextField1.setEnabled(true);
        pr.getLobby().jTextField1.setText("");
        pr.openLobby();
        pr.setisConnected(false);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                pr.getLobby().jButton1.setEnabled(true);
            }
        });

    }
}
