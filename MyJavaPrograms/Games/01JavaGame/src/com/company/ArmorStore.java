package com.company;

import java.util.ArrayList;

public class ArmorStore implements IStore {

    private static ArrayList<Armor> armors = new ArrayList<>();

    public static ArrayList<Armor> getArmors() {
        return armors;
    }

    @Override
    public void initializeStore() {
        this.armors.add(new Armor("Plastic", 10, 25));
        this.armors.add(new Armor("Decent Armor", 20, 60));
        this.armors.add(new Armor("Elite Armor", 50, 200));
        this.armors.add(new Armor("Nuclear Armor", 100, 500));
    }

    @Override
    public void printItemList() {
        System.out.println("\nWelcome to the armor store! We currently selling:");

        for (int i = 0; i < armors.size(); i++) {
            Armor currentArmor = armors.get(i);
            System.out.println(i + ". " + currentArmor.getName() + "    Resistance: " + currentArmor.getArmor() + ", Price: " + currentArmor.getPrice());
        }
    }

    @Override
    public void printOptions() {
        System.out.println( "\nOptions:\n" +
                "1 - Buy armor\n" +
                "3 - Go back to main menu\n" +
                "Please indicate your options:");
    }

    @Override
    public int getItemPrice(int i) {
        return armors.get(i).getPrice();
    }

    @Override
    public String getItemName(int i) {
        return armors.get(i).getName();
    }

    @Override
    public int getItemCharacteristics(int i) {
        return armors.get(i).getArmor();
    }
}
