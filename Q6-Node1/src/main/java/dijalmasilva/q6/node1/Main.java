/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dijalmasilva.q6.node1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dijalma Silva <dijalmacz@gmail.com>
 */
public class Main {

    static int x = 0;
    static int y = 0;

    public static void main(String[] args) {

//        Thread b = new Thread() {
//            @Override
//            public void run() {
//                sum();
//            }
//        };
        for (int i = 0; i < 11; i++) {

            new Thread() {
                @Override
                public void run() {
                    try {
                        sum();
                        if (x % 2 == 0){
                            this.wait();
                        }else{
                            this.notify();
                        }
                    } catch (InterruptedException ex) {
                        
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    sum();
                }
            }.start();

            System.out.println(x);
        }
    }

    public static void sum() {
        x += 1;
        y += 1;
    }
}
