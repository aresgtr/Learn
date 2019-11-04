package com.company;

import java.util.function.Function;

public class Challenge4 {

    public static void main(String[] args) {

        Function<String, String> lambdaFunction = s -> {
            StringBuilder returnVal = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i % 2 == 1) {
                    returnVal.append(s.charAt(i));
                }
            }
            return returnVal.toString();
        };


        //  Challenge 5
        System.out.println(everySecondCharacter(lambdaFunction, "123456789"));
    }

    public static String everySecondCharacter(Function<String, String> func, String source) {
        return func.apply(source);
    }
}
