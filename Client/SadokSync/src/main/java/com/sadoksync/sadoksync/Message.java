/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import java.io.Serializable;

/**
 *
 * @author Pontus
 */
public class Message implements Serializable {

    String name;
    String type;
    String ipAddr;
    String text;

    public Message() {

    }

    Message(Comunity com) {
        this.name = com.getComunityName();
        this.type = "Comunity Registration";
        this.ipAddr = com.getHost();
        this.text = com.getTopic();
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
}
