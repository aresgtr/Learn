package com.company;

import static com.company.Main.EOF;

import java.util.concurrent.ArrayBlockingQueue;

public class MyConsumer implements Runnable {
    private ArrayBlockingQueue<String> buffer;
    private String color;

    MyConsumer(ArrayBlockingQueue<String> buffer, String color) {
        this.buffer = buffer;
        this.color = color;
    }

    public void run() {

        int counter = 0;
        while (true) {
            synchronized (buffer) {
                try {
                    if (buffer.isEmpty()) {
                        continue;
                    }
                    System.out.println(color + "The counter = " + counter);
                    counter = 0;

                    if (buffer.peek().equals(EOF)) {
                        System.out.println(color + "Exiting");
                        break;
                    } else {
                        System.out.println(color + "Removed " + buffer.take());
                    }
                } catch (InterruptedException e) {

                }
            }
        }

    }
}
