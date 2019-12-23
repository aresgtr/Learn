package com.company;

import javax.swing.*;

public class ConstructorPractice {

    public static void main(String[] args) {
	// write your code here
        Car car = new Car(1.8, 4, "Black");
        car.printCar();
    }

}

class Car {
    double engine;
    int doors;
    String color;

    public Car(double engine, int doors, String color) {
        this(engine, doors);
        this.color = color;
    }

    public void printCar() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("Engine: " + engine);
        System.out.println("Doors: " + doors);
        System.out.println("Color: " + color);
        System.out.println();
    }
}