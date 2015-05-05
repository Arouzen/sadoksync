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
        pr.getClient().getPublicPlaylist().removefromPlaylist(pr.getNick());
        //byt host
        if (pr.isHost()) {
            pr.removePeerbyNick(pr.getNick());
            stm.killStream();
            if (pr.com.isEmpty()) {
                //deregister from registry
                
                Message deRegister = new Message();
                deRegister.setType("deRegister");
                deRegister.setName(pr.com.getComunityName());
                
                pr.sendMsg(pr.com.getRegistryAddr(), 3333, deRegister);
                
            } else {
                Message removeHost = new Message();
                removeHost.setType("removePeerFromCommunity");
                removeHost.setName(pr.getNick());
                pr.sendMsgToComunity(removeHost);

                //Simply to migrate the stream
                pr.getClient().startStream();
            };

            //pr.SendPMap(pr.getMyIp());
            
            
        } else {
            Message msg = new Message();
            msg.setType("removePeerbyNick");
            msg.setName(pr.getNick());
            pr.sendMsg(pr.getHost(), 4444, msg);
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        pr.com.clearOldCommunity();
        pr.openLobby();
    }
}
