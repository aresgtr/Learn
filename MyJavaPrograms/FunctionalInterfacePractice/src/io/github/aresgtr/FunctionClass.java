package io.github.aresgtr;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionClass {

    public static void main(String[] args) {

        System.out.println("Function Test >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Function<Integer, String> intToString = a -> {
            if (a == 0)
                return "zero";
            if (a == 1)
                return "one";
            if (a == 2)
                return "two";
            if (a == 3)
                return "three";
            else
                return "unknown";
        };

        System.out.println(intToString.apply(1));
        System.out.println(intToString.apply(99));


        System.out.println("BiFunction Test >>>>>>>>>>>>>>>>>>>>>>>>>>>");

        BiFunction<Integer, Integer, String> intAddToString = (a, b) -> {
            int result = a + b;

            if (result == 0)
                return "zero";
            if (result == 1)
                return "one";
            if (result == 2)
                return "two";
            if (result == 3)
                return "three";
            else
                return "unknown";
        };

        System.out.println(intAddToString.apply(1, 1));
        System.out.println(intAddToString.apply(1, 5));



    }

}
