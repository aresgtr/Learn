package com.company;

import java.util.ArrayList;
import java.util.List;

public class WeaponStore implements IStore {

    private static ArrayList<Weapon> weaponList = new ArrayList<>();

    public static ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }

    @Override
    public void initializeStore() {
        this.weaponList.add(new Weapon("Stick", 2, 10, 1));
        this.weaponList.add(new Weapon("Baseball Bat", 5, 20, 1));
        this.weaponList.add(new Weapon("Knife", 10, 50, 1));
        this.weaponList.add(new Weapon("Sword", 30, 100, 1));
        this.weaponList.add(new Weapon("Pistol", 50, 200, 1));
    }

    @Override
    public void printItemList() {
        System.out.println("Welcome to the weapon store! We currently selling:");

        for (int i = 0; i < weaponList.size(); i++) {
            Weapon currentWeapon = weaponList.get(i);
            System.out.println(i + ". " + currentWeapon.name + ", Damage: " + currentWeapon.damage + ", Price: " + currentWeapon.price);
        }
    }

    @Override
    public void printOptions() {
        System.out.println( "\nOptions:\n" +
                "1 - Buy weapon\n" +
                "2 - Sell weapon\n" +
                "3 - Go back to main menu\n" +
                "Please indicate your options:");
    }

    @Override
    public int getItemPrice(int i) {
        return weaponList.get(i).getPrice();
    }

    @Override
    public String getItemName(int i) {
        return weaponList.get(i).getName();
    }

    @Override
    public int getItemCharacteristics(int i) {
        return weaponList.get(i).getDamage();
    }




    //  Inner Class Weapon
    private class Weapon {
        private String name;
        private int damage;
        private int price;
        private int level;

        public Weapon(String name, int damage, int price, int level) {
            this.name = name;
            this.damage = damage;
            this.price = price;
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public int getDamage() {
            return damage;
        }

        public int getPrice() {
            return price;
        }

        public int getLevel() {
            return level;
        }
    }
}
