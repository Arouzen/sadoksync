/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

import com.sadoksync.msg.ClientRemoteInterface;
import java.util.HashMap;

/**
 *
 * @author Pontus
 */
public class PeerMap {
    HashMap<String,PeerReg> pMap;

    public PeerMap(){
        pMap = new HashMap<String,PeerReg>();
    }
    
    public synchronized void put(String name, PeerReg pr){
        pMap.put(name, pr);
    }
    public synchronized PeerReg get(String name){
        return pMap.get(name);
    }
}
