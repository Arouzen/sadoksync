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
public class Driver {

    Peer pr;

    public static void main(String[] args) {
        boolean gui = true;
        for (String arg : args) {
            if (arg.equals("nogui")) {
                System.out.println("NO GUI");
                gui = false;
            }
        }
        new Driver(gui);
    }

    public Driver(boolean gui) {

        pr = new Peer();
        pr.run(gui);

    }
}
