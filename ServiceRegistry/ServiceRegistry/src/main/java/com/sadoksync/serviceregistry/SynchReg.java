package com.sadoksync.serviceregistry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import com.sadoksync.message.ComunityRegistration;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Pontus
 */
public class SynchReg implements Serializable{
    HashMap<String,ComunityRegistration> criMap;

    public SynchReg(){
        criMap = new HashMap<String,ComunityRegistration>();
    }
    
    public synchronized void put(String name, ComunityRegistration cr){
        criMap.put(name, cr);
    }
    public synchronized ComunityRegistration get(String name){
        return criMap.get(name);
    }

    public synchronized List getKeyList() {
       List<String> li;
       li = new ArrayList<String>(criMap.keySet());
       return li;
    }
}
