package com.company;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static WeaponStore weaponStore = new WeaponStore();
    private static ShieldStore shieldStore = new ShieldStore();
    private static ArmorStore armorStore = new ArmorStore();
    private static Player player = new Player("");
    private static Levels levels = new Levels();
    private static int currentLevel = 0;

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
        System.out.println( "\nHealth: " + player.getHealth() +
                            " | Armor: " + player.getCurrentArmor() +
                            " | Money: " + player.getMoney() +
                            "\nWeapon: " + player.getWeapon() +
                            " | Attack: " + player.getCurrentAttack() +
                            " | Shield: " + player.getShield() +
                            " | Resistance: " + player.getCurrentShield() + "\n");
    }


    private static void gameInitialization() {
        weaponStore.initializeStore();
        shieldStore.initializeStore();
        armorStore.initializeStore();
        levels.initializeLevels();
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
                    buyWeapon();
                    printMenu();
                    break;

                case 2:
                    buyShield();
                    printMenu();
                    break;

                case 3:
                    buyArmor();
                    printMenu();
                    break;

                case 4:
                    attackEnemy();
                    printMenu();
                    break;
            }
        }
    }


    /*
     * This section contains functions for Weapon Store
     */

    private static void buyWeapon() {
        weaponStore.printItemList();
        System.out.println("Please indicate which weapon to buy, 999 to quit to main menu:");
        int weaponToBuy = scanner.nextInt();
        scanner.nextLine();
        if (weaponToBuy == 999) {
            return;
        }
        if (weaponToBuy > WeaponStore.getWeaponList().size() - 1) {
            System.out.println("Invalid input.");
        } else {
            //  main job
            if (player.getMoney() < weaponStore.getItemPrice(weaponToBuy)) {
                System.out.println("Insufficient funds");
            } else {
                //  Buy!
                player.setMoney(player.getMoney() - weaponStore.getItemPrice(weaponToBuy));
                player.setWeapon(weaponStore.getItemName(weaponToBuy));
                player.setCurrentAttack(weaponStore.getItemCharacteristics(weaponToBuy));
            }
        }
    }



    /*
     * This section contains function for shield store
     */

    private static void buyShield() {
        shieldStore.printItemList();
        System.out.println("Please indicate which shield to buy, 999 to quit to main menu:");
        int shieldToBuy = scanner.nextInt();
        scanner.nextLine();
        if (shieldToBuy == 999) {
            return;
        }
        if (shieldToBuy > shieldStore.getShieldList().size() - 1) {
            System.out.println("Invalid input.");
        } else {
            //  main job
            if (player.getMoney() < shieldStore.getItemPrice(shieldToBuy)) {
                System.out.println("Insufficient funds");
            } else {
                //  Buy!
                player.setMoney(player.getMoney() - shieldStore.getItemPrice(shieldToBuy));
                player.setShield(shieldStore.getItemName(shieldToBuy));
                player.setCurrentShield(shieldStore.getItemCharacteristics(shieldToBuy));
            }
        }
    }


    /*
     * This section contains function for armor store
     */

    private static void buyArmor() {
        armorStore.printItemList();
        System.out.println("Please indicate which armor to buy, 999 to quit to main menu:");
        int armorToBuy = scanner.nextInt();
        scanner.nextLine();
        if (armorToBuy == 999) {
            return;
        }
        if (armorToBuy > armorStore.getArmors().size() - 1) {
            System.out.println("Invalid input.");
        } else {
            //  main job
            if (player.getMoney() < armorStore.getItemPrice(armorToBuy)) {
                System.out.println("Insufficient funds");
            } else {
                //  Buy!
                player.setMoney(player.getMoney() - armorStore.getItemPrice(armorToBuy));
                player.setCurrentArmor(armorStore.getItemCharacteristics(armorToBuy));
            }
        }
    }


    //  This is for creating the missions
    private static void attackEnemy() {
        String name = levels.getEnemyName(currentLevel);
        int health = levels.getEnemyHealth(currentLevel);
        int attack = levels.getEnemyAttack(currentLevel);
        int shield = levels.getEnemyShield(currentLevel);

        printStatus();

        System.out.println( "=================================\n*********************************\n" +
                            name + "    |   " +
                            "Health: " + String.valueOf(health) + "    |   " +
                            "Attack: " + String.valueOf(attack)+ "    |   " +
                            "Shield: " + String.valueOf(shield) + "\n" +
                            "Are you ready? (yes/no)");

        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            boolean playerAlive = true;
            boolean whosTurn = false; //false for enemy's turn and true for player's turn
            int currentAttack;
            int currentShield;
            int enemyHealth = levels.getEnemyHealth(currentLevel);
            int sufferedAttack;

            while (playerAlive) {
                if (!whosTurn) {
                    //Enemy's turn
                    currentAttack = generateRandom(attack);
                    currentShield = generateRandom(player.getCurrentShield());
                    if (currentShield < currentAttack) {
                        sufferedAttack = currentAttack - currentShield;
                        if (player.getCurrentArmor() >= sufferedAttack) {
                            player.setCurrentArmor(player.getCurrentArmor() - sufferedAttack);
                        } else {
                            player.setHealth(player.getHealth() - (sufferedAttack - player.getCurrentArmor()));
                            player.setCurrentArmor(0);
                        }
                        if (player.getHealth() < 0) {
                            player.setHealth(0);
                        }
                    }
                    whosTurn = true;
                    if (player.getHealth() <= 0) {
                        playerAlive = false;
                    }
                } else {
                    //Player's turn
                    currentAttack = generateRandom(player.getCurrentAttack());
                    currentShield = generateRandom(shield);
                    if (currentShield < currentAttack) {
                        enemyHealth -= (currentAttack - currentShield);
                        if (enemyHealth < 0) {
                            enemyHealth = 0;
                        }
                    }
                    whosTurn = false;
                    System.out.println(player.getName() + " H: " + player.getHealth() + "  A: " + player.getCurrentArmor() + "   |   " + levels.getEnemyName(currentLevel) + " H: " + enemyHealth);
                    if (enemyHealth <= 0) {
                        System.out.println("Mission success!\n*********************************\n=================================\n");
                        player.setMoney(player.getMoney() + levels.pay(currentLevel));
                        if (currentLevel < 9) {
                            currentLevel++;
                        } else {
                            System.out.println("Game Complete!");
                        }
                        return;
                    }
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Mission Failed! But don't give up.\nRemember: Failure is the mother of success!\n*********************************\n=================================\n");
            currentLevel = 0;
            player.resetPlayer();

        } else {
            System.out.println("\nYou are not confident! Come back when you are ready.\n*********************************\n=================================\n");
        }
    }

    private static int generateRandom(int max) {
        return (int) (Math.random() * max);
    }
}
