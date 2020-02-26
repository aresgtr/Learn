package com.company;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicatePractice {

    public static void main(String[] args) {

//        Function<Integer, Integer> adder = a -> a + 1;
//
//        System.out.println(adder.apply(1));

        Predicate<Integer> p1 = a -> a > 5;

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        list.stream()
                .filter(p1)
                .forEach(System.out::println);
    }
}
