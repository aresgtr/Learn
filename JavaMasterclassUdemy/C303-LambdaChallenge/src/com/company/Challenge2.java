package com.company;

import java.util.function.Function;

public class Challenge2 {

    public static void main(String[] args) {
        System.out.println(everySecondChar("123456"));


        /*
         * This is the solution
         */
        Function<String, String> lambdaFunction = s -> {
            StringBuilder returnVal = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i % 2 == 1) {
                    returnVal.append(s.charAt(i));
                }
            }
            return returnVal.toString();
        };
    }









    /*
     * This is the method to be converted to lambda expression
     */
    public static String everySecondChar(String source) {
        StringBuilder returnVal = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            if (i % 2 == 1) {
                returnVal.append(source.charAt(i));
            }
        }
        return returnVal.toString();
    }
}
