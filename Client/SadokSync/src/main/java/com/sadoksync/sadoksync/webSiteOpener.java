/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sadoksync.sadoksync;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author ibo
 */
public class webSiteOpener {
    public void openMyWebSite(String mySiteLink){
        try{
            Process p ;
            p = Runtime.getRuntime().exec("cmd /c start "+ mySiteLink);
        }
        catch(IOException e ){
            JOptionPane.showMessageDialog(null,"Error: " +e);
        }
    }
}
