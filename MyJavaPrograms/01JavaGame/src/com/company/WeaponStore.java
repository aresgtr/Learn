package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class WeaponStore {

    private static ArrayList<Weapon> weaponList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);


    public WeaponStore() {

    }

    public List<Weapon> printWeaponList() {
        System.out.println("Welcome to the weapon store! We currently selling:");

        for (int i = 0; i < weaponList.size(); i++) {
            Weapon currentWeapon = weaponList.get(i);
            System.out.println(i + ". " + currentWeapon.name + ", Damage: " + currentWeapon.damage + ", Price: " + currentWeapon.price);
        }

        return weaponList;
    }

    public static ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }

    public int getWeaponPrice(int i) {
        return weaponList.get(i).getPrice();
    }

    public String getWeaponName(int i) {
        return weaponList.get(i).getName();
    }

    public int getWeaponAttack(int i) {
        return weaponList.get(i).getDamage();
    }

    public void inputOptions() {
        System.out.println( "\nOptions:\n" +
                            "1 - Buy weapon\n" +
                            "2 - Sell weapon\n" +
                            "3 - Go back to main menu\n" +
                            "Please indicate your options:");
    }

    private void buyWeapon() {
        System.out.println("Buying weapon");
    }

    private void sellWeapon() {
        System.out.println("Selling weapon");
    }

    public void initializeWeaponStore() {
        this.weaponList.add(new Weapon("Stick", 2, 10, 1));
        this.weaponList.add(new Weapon("Baseball Bat", 5, 20, 1));
        this.weaponList.add(new Weapon("Knife", 10, 50, 1));
        this.weaponList.add(new Weapon("Sword", 30, 100, 1));
        this.weaponList.add(new Weapon("Ultimate Battle Weapon", 50, 200, 1));
    }

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
