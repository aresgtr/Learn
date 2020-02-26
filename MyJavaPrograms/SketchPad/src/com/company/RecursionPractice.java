package com.company;

public class RecursionPractice {

    public static void main(String[] args) {

        printStar(10);
    }

    private static void printStar (int number) {

        int i = 0;

        for (int j = 0; j < number; j++) {
            System.out.print("*");
        }
        System.out.println();

        if (i < number) {
            printStar(number - 1);
        }

        System.out.println("hi");
    }
}
