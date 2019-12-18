package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }

    public static void printYearsAndDays (long minutes) {
        if (minutes < 0) {
            System.out.println("Invalid Value");
        } else {
            long day = minutes / 60 / 24;
            long year = day / 365;
            day = day % 365;

            System.out.println(minutes + " min = " + year + " y and " + day + " d");
        }
    }
}
