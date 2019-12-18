package com.company;

public class DogMain {
    public static void main(String[] args) {
        Labrador rover = new Labrador("Rover");
        Dog rover2 = new Dog("Rover");

        System.out.println(rover2.equals(rover)); // Labrador is an instance of Dog
        System.out.println(rover.equals(rover2));  // Dog isn't instance of Labrador
    }
}
