package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverrideAndGenerics {
    public static void main(String[] args) {
        CollectionOfVehicles<Sedan, Male> sedanClub = new CollectionOfVehicles<>("The Sedan Club");
        Sedan vwPassat = new Sedan("VW Passat");
        Sedan volvoS50 = new Sedan("Volvo S60");
        Sedan bmw330 = new Sedan("BMW 330");
        Pickup f150 = new Pickup("Ford F-150");

        Male mike = new Male("Mike", 30);
        Male joe = new Male("Joe", 40);
        Male jack = new Male("Jack", 25);

        sedanClub.addEntry(mike, vwPassat);
        sedanClub.addEntry(joe, volvoS50);
        sedanClub.addEntry(jack, bmw330);

        System.out.println(sedanClub.toString());
    }
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
        return name;
    }
}

class CollectionOfVehicles<T extends Vehicle, H extends Human> {
    private String name;
    private Map<H, T> vehicles;


    public CollectionOfVehicles(String name) {
        this.name = name;
        this.vehicles = new HashMap();
    }

    public void addEntry(H owner, T vehicle) {
        vehicles.put(owner, vehicle);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getName() + ":\n");
        vehicles.forEach((owner, vehicle) -> result.append(owner.toString() + "  " + vehicle.getName() + "\n"));
        return result.toString();
    }
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

    public Sedan(String name) {
        super(VehicleType.SEDAN, name);
    }

    @Override
    public String getType() {
        return "sedan";
    }

    @Override
    public String getName() {
        return super.getName();
    }
}

class Human {
    private String name;
    private int age;

    public Human(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

class Male extends Human {

    private final String gender = "Male";

    public Male(String name, int age) {
        super(name, age);
    }

    @Override
    public String toString() {
        return getName() + ", Age: " + getAge() + ", Gender: " + gender;
    }
}