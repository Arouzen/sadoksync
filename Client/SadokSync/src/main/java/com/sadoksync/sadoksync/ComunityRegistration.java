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
    ClientRemoteInterface cri;
    String topic;
    String name;
    public ComunityRegistration(String name, ClientRemoteInterface cri, String topic){
        this.name = name;
        this.cri = cri;
        this.topic = topic;
    }

    public ClientRemoteInterface getHost() {
        return this.cri;
    }
    
    public String getTopic(){
        return topic;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(){
        this.name = name;
    }
    
    public void setHost(ClientRemoteInterface cri){
        this.cri = cri;
    }
    public void setTopic(String topic){
        this.topic = topic;
    }
}
