package com.company;

public class Player {
    private String name;
    private int health;
    private int money;
    private String weapon;
    private String shield;
    private int currentAttack;
    private int currentShield;
    private int currentArmor;

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.money = 50;
        this.weapon = "Fist";
        this.shield = "None";
        this.currentAttack = 1;
        this.currentShield = 0;
        this.currentArmor = 0;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMoney() {
        return money;
    }

    public String getWeapon() {
        return weapon;
    }

    public String getShield() {
        return shield;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public int getCurrentShield() {
        return currentShield;
    }

    public int getCurrentArmor() {
        return currentArmor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }

    public void setCurrentAttack(int currentAttack) {
        this.currentAttack = currentAttack;
    }

    public void setCurrentShield(int currentShield) {
        this.currentShield = currentShield;
    }

    public void setCurrentArmor(int currentArmor) {
        this.currentArmor = currentArmor;
    }

    public void resetPlayer() {
        this.health = 100;
        this.money = 50;
        this.weapon = "Fist";
        this.shield = "None";
        this.currentAttack = 1;
        this.currentShield = 0;
        this.currentArmor = 0;
    }
}
