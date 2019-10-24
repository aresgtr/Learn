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
        System.out.println( "Please select following:\n" +
                            "1 - visit weapon store\n" +
                            "2 - visit shield store\n" +
                            "3 - visit armor store\n" +
                            "4 - attack next enemy!\n" +
                            "0 - quit game");
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

    private static void visitWeaponStore() {
        weaponStore.getWeaponList();
        weaponStore.inputOptions();
    }

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
