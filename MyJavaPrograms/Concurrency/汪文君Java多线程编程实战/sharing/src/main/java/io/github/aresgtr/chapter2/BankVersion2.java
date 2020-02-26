package io.github.aresgtr.chapter2;

public class BankVersion2 {

    public static void main(String[] args) {

        final TicktWindowRunnable ticktWindow = new TicktWindowRunnable();  //  One instance! Runnable is good!

        Thread windowThread1 = new Thread(ticktWindow, "1号窗口");
        Thread windowThread2 = new Thread(ticktWindow, "2号窗口");
        Thread windowThread3 = new Thread(ticktWindow, "3号窗口");
        Thread windowThread4 = new Thread(ticktWindow, "4号窗口");

        windowThread1.start();
        windowThread2.start();
        windowThread3.start();
        windowThread4.start();
    }
}
