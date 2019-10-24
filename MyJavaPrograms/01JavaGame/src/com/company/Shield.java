package com.company;

public class Shield {
    private String name;
    private int price;
    private int resistance;

    public Shield(String name, int price, int resistance) {
        this.name = name;
        this.price = price;
        this.resistance = resistance;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getResistance() {
        return resistance;
    }
}
