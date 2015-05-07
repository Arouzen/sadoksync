/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.message;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pontus
 */
public class RegistryMessage implements Serializable {

    String name;
    String type;
    String ipAddr;
    String text;
    String uuid;
    List li;
    
    public RegistryMessage() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
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

    public void setText(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setList(List li) {
        this.li = li;
    }

    public List getList() {
        return li;
    }

    String getUUID() {
        return uuid;
    }

    void setUUID(String uuid) {
        this.uuid = uuid;
    }

}
