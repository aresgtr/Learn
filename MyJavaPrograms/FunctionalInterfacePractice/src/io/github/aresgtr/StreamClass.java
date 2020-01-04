package io.github.aresgtr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @URL  https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
 * @ToDo flatMap
 */

public class StreamClass {

    public static void main(String[] args) {

        List<String> legoSets = Arrays.asList(
                "Palace Cinema",
                "Parisian Restaurant",
                "Detective Office",
                "Brick Bank",
                "Assembly Square",
                "Downtown Diner"
        );

        legoSets
                .stream()
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("P"))
                .sorted()
                .forEach(System.out::println);

        System.out.println();

        Stream.of("A", "B", "C")
                .findFirst()
                .ifPresent(System.out::println);

        System.out.println();

        legoSets
                .stream()
                .map(a -> a + " is a good Lego Set")
                .sorted()
                .findFirst()
                .ifPresent(System.out::println);


        System.out.println(">>>>>>>>>Collect");
        //  Collect
        List<String> transLegoSets = legoSets
                .stream()
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());

        System.out.println(transLegoSets);


        System.out.println(">>>>>>>>>>FlatMap");
        //  FlatMap
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Audi"));
        cars.add(new Car("BMW"));

        cars.stream()
                .flatMap(c -> c.parts.stream())
                .forEach(p -> System.out.println(p.name));
    }
}


class Car {
    String name;
    List<CarParts> parts = new ArrayList<>();

    public Car(String name) {
        this.name = name;
        this.parts.add(new CarParts(name + " engine"));
        this.parts.add(new CarParts(name + " doors"));
        this.parts.add(new CarParts(name + " wheels"));
    }
}

class CarParts {
    String name;

    public CarParts(String name) {
        this.name = name;
    }
}
