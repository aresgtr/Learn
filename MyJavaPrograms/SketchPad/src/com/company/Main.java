package com.company;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        numbers
                .stream()
                .forEach(System.out::println);

        System.out.println(">>>>> Parallel");

        numbers
                .stream()
                .parallel()
                .forEach(System.out::println);

        System.out.println(">>>>> Parallel Stream");

        numbers
                .parallelStream()
                .forEach(System.out::println);

        System.out.println(">>>>> Find First");
        numbers
                .parallelStream()
                .findFirst()
                .ifPresent(System.out::println);
    }
}
