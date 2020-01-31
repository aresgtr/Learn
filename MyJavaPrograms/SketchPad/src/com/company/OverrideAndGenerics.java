package com.company;

import java.util.ArrayList;
import java.util.List;

public class OverrideAndGenerics {
    public static void main(String[] args) {
        CollectionOfVehicles<Sedan> sedanClub = new CollectionOfVehicles<>("The Sedan Club");
        Sedan vwPassat = new Sedan("VW Passat");
        Sedan volvoS50 = new Sedan("Volvo S60");
        Sedan bmw330 = new Sedan("BMW 330");
        Pickup f150 = new Pickup("Ford F-150");

        sedanClub.addVehicle(vwPassat, volvoS50, bmw330);

        System.out.println(sedanClub.toString());

        CollectionOfVehicles<Vehicle> vehicleClub = new CollectionOfVehicles<>("The Vehicle Club");
        vehicleClub.addVehicle(vwPassat, f150);
        System.out.println(vehicleClub.toString());
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

class CollectionOfVehicles<T extends Vehicle> {
    private String name;
    private List<T> vehicles;

    public CollectionOfVehicles(String name) {
        this.name = name;
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(T...vehicle) {
        for (T whatever: vehicle) {
            vehicles.add(whatever);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getName() + ":\n");
        vehicles.forEach(a -> result.append(a.getName() + "\n"));
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