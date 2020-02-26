package com.company;

public class Armor {
    private String name;
    private int price;
    private int armor;

    public Armor(String name, int price, int armor) {
        this.name = name;
        this.price = price;
        this.armor = armor;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getArmor() {
        return armor;
    }
}
