package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here

        /*
         * This is the original Anonymous runnable class
         */
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String myString = "Let's split this up into an array";
                String[] parts = myString.split(" ");
                for (String part : parts) {
                    System.out.println(part);
                }
            }
        };
        runnable.run();

        System.out.println("\n\n");
        /*
         * Let's convert it to lambda expression
         */
        Runnable runnable1 = () -> {
            String myString = "Let's split this up into an array";
            String[] parts = myString.split(" ");
            for (String part : parts) {
                System.out.println(part);
            }
        };
        runnable1.run();


    }
}
