package com.company;

import java.util.function.Supplier;

public class Challenge6 {

    public static void main(String[] args) {

//        Supplier<String> iLoveJava = () -> "I love Java!";
        Supplier<String> iLoveJava = () -> {
            return "I love Java!";
        };

        //  Challenge 7
        String supplierResult = iLoveJava.get();
        System.out.println(supplierResult);
    }
}
