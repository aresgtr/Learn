package com.company;

/*
 * 这个Project是关于我的多线程练习，这里有个非常好的材料供阅读
 * https://blog.csdn.net/Evankaka/article/details/44153709
 *
 * 一、扩展java.lang.Thread类
 */
public class Main {

    public static void main(String[] args) {
	// write your code here
        Thread1 t1 = new Thread1("A");
        Thread1 t2 = new Thread1("B");

        t1.start();
        t2.start();
    }
}

class Thread1 extends Thread {
    private String name;

    public Thread1(String name) {
        this.name = name;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(name + "运行  :  " + i);
            try {
                sleep((int) Math.random() * 10);        //  sleep() is the same as Thread.sleep()
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }

}