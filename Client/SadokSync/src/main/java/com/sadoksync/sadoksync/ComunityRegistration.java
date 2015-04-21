package com.sadoksync.sadoksync;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.sadoksync.msg.ClientRemoteInterface;
import com.sadoksync.msg.ClientRemoteInterface;
import java.io.Serializable;

/**
 *
 * @author Pontus
 */
public class ComunityRegistration implements Serializable {
    String ipAddr;
    String topic;
    String name;
    public ComunityRegistration(String name, String ipAddr, String topic){
        this.name = name;
        this.ipAddr = ipAddr;
        this.topic = topic;
    }

    public String getHost() {
        return this.ipAddr;
    }
    
    public String getTopic(){
        return this.topic;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(){
        this.name = name;
    }
    
    public void setHost(String ipAddr){
        this.ipAddr = ipAddr;
    }
    public void setTopic(String topic){
        this.topic = topic;
    }
}
