/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

/**
 *
 * @author Pontus
 */
public class DebugSys {

    boolean mode;

    public DebugSys() {
        this.mode = false;
    }

    public void debug(boolean mode) {
        this.mode = mode;
    }

    public void println(String str) {
        if (mode) {
            System.out.println(str);
        }
    }

    public void print(String str) {
        if (mode) {
            System.out.print(str);
        }
    }
}
