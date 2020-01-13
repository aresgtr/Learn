package com.qi.concurrency.chapter1;

public class TryConcurrency {

    public static void main(String[] args) {

        Thread t1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Task 1 => " + i);
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Task 2 => " + i);
                }
            }
        };

        t1.start();
        t2.start();
    }


}
