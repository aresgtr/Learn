package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        printMegaBytesAndKiloBytes(2500);
        System.out.println();
        printMegaBytesAndKiloBytes(-1024);
        System.out.println();
        printMegaBytesAndKiloBytes(5000);
    }

    public static void printMegaBytesAndKiloBytes (int kiloBytes) {
        if (kiloBytes < 0) {
            System.out.printf("Invalid Value");
        } else {
            int megabytes = kiloBytes / 1024;
            int remain = kiloBytes % 1024;
            System.out.printf(kiloBytes + " KB = " + megabytes + " MB and " + remain + " KB");
        }
    }
}
