/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dijalmasilva.q4.node1;

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

        for (int i = 0; i < 10; i++) {
            
            new Thread() {
                @Override
                public void run() {
                    sum();
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
