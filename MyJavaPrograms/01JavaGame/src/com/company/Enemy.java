package com.company;

import java.util.ArrayList;

public class Enemy {
    private String name;
    private int health;
    private int attack;
    private int shield;
    private int pay;

    public Enemy(String name, int health, int attack, int shield, int pay) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.shield = shield;
        this.pay = pay;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getShield() {
        return shield;
    }

    public int getPay() {
        return pay;
    }
}
