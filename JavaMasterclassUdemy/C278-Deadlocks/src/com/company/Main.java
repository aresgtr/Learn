package com.company;

public class Main {

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    public static void main(String[] args) {
	// write your code here
        new Thread1().start();
        new Thread2().start();
    }

    /*
        To avoid deadlock:
                    1. Synchronize locks in the same order 按同一顺序上锁
                    1. Use lock objects instead of synchronized blocks (Try lock)
     */


    private static class Thread1 extends Thread {
        public void run() {
            synchronized (lock1) {
                System.out.println("Thread 1: Has lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
                System.out.println("Thread 1: Waiting for lock2");
                synchronized (lock2) {
                    System.out.println("Thread 1: Has lock1 and lock2");
                }
                System.out.println("Thread 1: Released lock2");
            }
            System.out.println("Thread 1: Released lock1, Exiting...");
        }
    }

    private static class Thread2 extends Thread {
        public void run() {
            synchronized (lock2) {
                System.out.println("Thread 2: Has lock2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
                System.out.println("Thread 2: Waiting for lock1");
                synchronized (lock1) {
                    System.out.println("Thread 2: Has lock22 and lock1");
                }
                System.out.println("Thread 2: released lock1");
            }
            System.out.println("Thread 2: Released lock2, Exiting...");
        }
    }
}






