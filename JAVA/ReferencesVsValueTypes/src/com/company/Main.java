package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	// write your code here

        int myIntValue = 10;
        int anotherIntValue = myIntValue;

        System.out.println("myIntValue = " + myIntValue);
        System.out.println("anotherIntValue = " + anotherIntValue);

        anotherIntValue++;

        System.out.println("myIntValue = " + myIntValue);
        System.out.println("anotherIntValue = " + anotherIntValue);

        int[] myIntArray = new int[5];
        int[] anotherArray = myIntArray;

        System.out.println("myIntArray = " + Arrays.toString(myIntArray));
        System.out.println("anotherIntArray = " + Arrays.toString(anotherArray));

        anotherArray[0] = 1;

        System.out.println("myIntArray = " + Arrays.toString(myIntArray));
        System.out.println("anotherIntArray = " + Arrays.toString(anotherArray));

        modifyArray(myIntArray);

        System.out.println("myIntArray = " + Arrays.toString(myIntArray));
        System.out.println("anotherIntArray = " + Arrays.toString(anotherArray));
    }

    private static void modifyArray(int[] array) {
        array[0] = 2;
    }
}
