package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OverrideAndGenerics {

}

enum VehicleType {
    SEDAN,
    COUPE,
    SUV,
    CROSSOVER,
    PICKUP,
    SEMI,
    COACH
}

class Vehicle {
    private VehicleType type;
    private String name;

    public Vehicle(VehicleType type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getType() {
        //  nop
        return null;
    }

    public String getName() {
        System.out.println("This is a vehicle!");
        return name;
    }
}

class CarClub <N extends Vehicle> {
    private String name;

    private ArrayList<N> vehicles = new ArrayList<>();

}

class Pickup extends Vehicle {

    public Pickup(String name) {
        super(VehicleType.PICKUP, name);
    }

    @Override
    public String getType() {
        return "pickup";
    }

    @Override
    public String getName() {
        return super.getName();
    }
}

class Sedan extends Vehicle {

    public Sedan(VehicleType type, String name) {
        super(type, name);
    }
}