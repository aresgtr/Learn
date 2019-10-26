package com.company;

import java.util.ArrayList;

public class ShieldStore implements IStore{

    private static ArrayList<Shield> shieldList = new ArrayList<>();

    public static ArrayList<Shield> getShieldList() {
        return shieldList;
    }

    @Override
    public void initializeStore() {
        this.shieldList.add(new Shield("Pot Lid", 10, 5));
        this.shieldList.add(new Shield("Toy Shield", 30, 25));
        this.shieldList.add(new Shield("Cavalry Shield", 50, 50));
        this.shieldList.add(new Shield("Elite Shield", 80, 100));
        this.shieldList.add(new Shield("Police Riot Shield", 120, 180));
    }

    @Override
    public void printItemList() {
        System.out.println("\nWelcome to the shield store! We currently selling:");

        for (int i = 0; i < shieldList.size(); i++) {
            Shield currentShield = shieldList.get(i);
            System.out.println(i + ". " + currentShield.getName() + ", Resistance: " + currentShield.getResistance() + ", Price: " + currentShield.getPrice());
        }
    }

    @Override
    public void printOptions() {
        System.out.println( "\nOptions:\n" +
                "1 - Buy shield\n" +
                "2 - Sell shield\n" +
                "3 - Go back to main menu\n" +
                "Please indicate your options:");
    }

    @Override
    public int getItemPrice(int i) {
        return shieldList.get(i).getPrice();
    }

    @Override
    public String getItemName(int i) {
        return shieldList.get(i).getName();
    }

    @Override
    public int getItemCharacteristics(int i) {
        return shieldList.get(i).getResistance();
    }
}
