package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Challenge11 {

    public static void main(String[] args) {

        /*
         * Use stream
         */
        List<String> topNames2015 = Arrays.asList(
                "Amelia",
                "Olivia",
                "emily",
                "Isla",
                "Ava",
                "oliver",
                "Jack",
                "Charlie",
                "harry",
                "Jacob"
        );

        List<String> firstUpperCaseList = new ArrayList<>();

        topNames2015
                .stream()
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
                .sorted(String::compareTo)
                .forEach(System.out::println);



        /*
         * This is for Challenge 12
         * The stream chain need to be modified to printout how many names begin with the letter 'A'
         */
        System.out.println("\nThis is Challenge 12:");
        long count = topNames2015
                .stream()
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
                .filter(name -> name.startsWith("A"))
                .count();

        System.out.println("Number of names beginning with A is: " + count);


        /*
         * This is for Challenge 13
         * Try to use peek to debug the output
         */
        System.out.println("\nThis is Challenge 13:");
        topNames2015
                .stream()
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
                .peek(System.out::println)
                .sorted(String::compareTo); //  Nothing outputs since there is no terminal operation

        /*
         * This is for Challenge 14
         * Add a terminal operation so that the the code outputs
         */
        System.out.println("\nThis is Challenge 14:");
        topNames2015
                .stream()
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
                .peek(System.out::println)
                .sorted(String::compareTo)  //  after peek(), so unsorted
                .count();   //  Terminal Operation
                            //  .collect works too
    }
}
