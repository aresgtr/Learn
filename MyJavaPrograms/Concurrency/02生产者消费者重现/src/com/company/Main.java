package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
	// write your code here
        List<String> 一堆小米 = new ArrayList<>();

        Producer 雷军 = new Producer(一堆小米, ThreadColor.ANSI_WHITE);
        Consumer 米粉1 = new Consumer(一堆小米, ThreadColor.ANSI_RED, "米粉1");
        Consumer 米粉2 = new Consumer(一堆小米, ThreadColor.ANSI_GREEN, "米粉2");
        Consumer 米粉3 = new Consumer(一堆小米, ThreadColor.ANSI_YELLOW, "米粉3");

        new Thread(雷军).start();
        new Thread(米粉1).start();
        new Thread(米粉2).start();
        new Thread(米粉3).start();
    }






    public static int generateRandom() {    //  Return an int from 0 to 1000
        Random random = new Random();
        return random.nextInt(1000);
    }
}
