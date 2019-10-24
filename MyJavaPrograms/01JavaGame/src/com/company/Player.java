package com.company;

public class Player {
    private String name;
    private int level; //   From 1 to 5, 5 as maximum
    private int health;
    private int money;

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.health = 100;
        this.money = 0;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public int getMoney() {
        return money;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
