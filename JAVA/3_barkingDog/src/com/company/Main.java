package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }

    public static boolean shouldWakeUp (boolean barking, int hourOfDay) {
        if (barking == true && (hourOfDay < 8 || hourOfDay > 22) && hourOfDay >= 0 && hourOfDay < 24) {
            return true;
        } else {
            return false;
        }
    }
}
