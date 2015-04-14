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
public class SynchReg {
    HashMap<String,ClientRemoteInterface> criMap;

    public SynchReg(){
        criMap = new HashMap<String,ClientRemoteInterface>();
    }
    
    public synchronized void put(String name, ClientRemoteInterface cri){
        criMap.put(name, cri);
    }
    public synchronized ClientRemoteInterface get(String name){
        return criMap.get(name);
    }
}
