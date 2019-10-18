package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
       System.out.println(areEqualByThreeDecimalPlaces(3.1756, 3.175));
    }

    public static boolean areEqualByThreeDecimalPlaces (double one, double two) {
        double oneTrimmed = ((long)(one * 1e3)) / 1e3;
        double twoTrimmed = ((long)(two * 1e3)) / 1e3;
        if (oneTrimmed == twoTrimmed) {
            return true;
        }
        return false;
    }
}
