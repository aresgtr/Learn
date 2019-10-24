package com.company;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static String name;
    private static WeaponStore weaponStore = new WeaponStore();
    private static Player player = new Player("");

    public static void main(String[] args) {
	    //  This is my first personal Java practice project. The project is a Java game with multiple text based interfaces.
        //  It should utilize as many java skills as possible.
        //  The game is a simple RPG with battles and levels and aims to be playable for more than 1 hour.
        gameInitialization();

        System.out.println("Welcome to the game! Please tell me your name:");
        player.setName(scanner.nextLine());

        System.out.println("Hi, " + player.getName() + "!");

        System.out.println("To begin your journey, please read the menu carefully.");
        printMenu();
        playGame();
    }

    private static void printMenu() {
        printStatus();
        System.out.println( "Please select following:\n" +
                            "1 - visit weapon store\n" +
                            "2 - visit shield store\n" +
                            "3 - visit armor store\n" +
                            "4 - attack next enemy!\n" +
                            "0 - quit game");
    }

    private static void printStatus() {
        System.out.println( "Health: " + player.getHealth() +
                            " Armor: " + player.getCurrentArmor() +
                            " Money: " + player.getMoney() +
                            " Weapon: " + player.getWeapon() +
                            " Attack: " + player.getCurrentAttack() +
                            " Shield: " + player.getCurrentShield());
    }


    private static void gameInitialization() {
        weaponStore.initializeWeaponStore();
    }

    private static void playGame() {
        boolean quit = false;

        while (!quit){
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0:
                    System.out.println("Game quiting...");
                    quit = true;
                    break;

                case 1:
                    visitWeaponStore();
                    printMenu();
                    break;

                case 2:
                    visitShieldStore();
                    printMenu();
                    break;

                case 3:
                    visitArmorStore();
                    printMenu();
                    break;

                case 4:
                    attackNextEnemy();
                    printMenu();
                    break;
            }
        }
    }



    /*
     * This section contains functions for Weapon Store
     */
    private static void visitWeaponStore() {
        printWeaponStoreOptions();
        boolean quit = false;
        while (!quit) {
            int action = scanner.nextInt();
            scanner.nextLine();
            switch (action) {
                case 1:
                    buyWeapon();
                    printWeaponStoreOptions();
                    break;

                case 2:
                    sellWeapon();
                    printWeaponStoreOptions();
                    break;

                case 3:
                    quit = true;
                    break;
            }
        }
    }

    private static void printWeaponStoreOptions() {
        System.out.println( "\nPlease input your option:\n" +
                "1 - buy weapon\n" +
                "2 - sell weapon\n" +
                "3 - back to main menu\n");
    }

    private static void buyWeapon() {
        weaponStore.printWeaponList();
        System.out.println("Please indicate which weapon to buy:\n");
        int weaponToBuy = scanner.nextInt();
        scanner.nextLine();
        if (weaponToBuy > weaponStore.getWeaponList().size() + 1) {
            System.out.println("Invalid input.");
        } else {
            //  main job
            if (player.getMoney() < weaponStore.getWeaponPrice(weaponToBuy)) {
                System.out.println("Insufficient funds");
            } else {
                //  Buy!
                player.setMoney(player.getMoney() - weaponStore.getWeaponPrice(weaponToBuy));
                player.setWeapon(weaponStore.getWeaponName(weaponToBuy));
                player.setCurrentAttack(weaponStore.getWeaponAttack(weaponToBuy));
                printStatus();
            }
        }
    }

    private static void sellWeapon() {
        System.out.println("Sell weapon");
    }






    /*
     * This section contains function for shield store
     */

    private static void visitShieldStore() {
        System.out.println("Welcome to the shield store!");
    }

    private static void visitArmorStore() {
        System.out.println("Welcome to the armor store!");
    }

    private static void attackNextEnemy() {
        System.out.println("Attack next enemy!");
    }
}
