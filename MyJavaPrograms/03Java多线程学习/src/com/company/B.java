package com.company;

/*
 * 二、实现java.lang.Runnable接口
 */
public class B {

    public static void main(String[] args) {
        Thread2 t1 = new Thread2("C");
        Thread2 t2 = new Thread2("D");

        new Thread(t1).start();
        new Thread(t2).start();
    }
}






class Thread2 implements Runnable {
    private String name;

    public Thread2(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(name + "运行  :  " + i);
            try {
                Thread.sleep((int) Math.random() * 10);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }
}
