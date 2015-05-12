/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pontus
 */
public class Message implements Serializable {

    String name;
    String type;
    String ipAddr;
    String text;
    String uuid;
    Message msg;
    List li = null;
    PublicPlaylist.Pair pair;
    PublicPlaylist playlist;
    private boolean bool;
    
    public Message() {

    }

    Message(Comunity com) {
        this.name = com.getComunityName();
        this.type = "Comunity Registration";
        this.ipAddr = com.getHost();
        this.text = com.getTopic();
        this.uuid = com.getUUID();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setipAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getipAddr() {
        return this.ipAddr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    public void setList(List li){
        this.li = li;
    }
    
    public List getList(){
        return li;
    }

    public void setPair(PublicPlaylist.Pair pair) {
        this.pair = pair;
    }
    public PublicPlaylist.Pair getPair(){
        return pair;
    }

    void setPlaylist(PublicPlaylist publicPlaylist) {
       this.playlist = publicPlaylist;
    }
    PublicPlaylist getPlaylist(){
        return this.playlist;
    }

    String getUUID() {
        return uuid;
    }

    void setUUID(String uuid) {
        this.uuid = uuid;
    }

    void setMessage(Message msg) {
        this.msg = msg;
    }
    
    Message getMessage() {
        return this.msg;
    }

    void setBoolean(boolean bool) {
        this.bool = bool;
    }
    
    boolean getBoolean() {
        return this.bool;
    }
    
}
