package com.company;

import java.util.function.Function;

public class Challenge3 {
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

        System.out.println(lambdaFunction.apply("123456789"));
    }
}
