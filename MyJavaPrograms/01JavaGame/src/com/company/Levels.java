package com.company;

import java.util.ArrayList;

public class Levels {

    private static ArrayList<Enemy> enemies = new ArrayList<>();

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void initializeLevels() {
        for (int i = 0; i < 10; i ++) {
            this.enemies.add(new Enemy("Enemy Level " + (i + 1), (i + 1) * 10, (i + 1) * 10, i * 10, (i + 1) * 10));
        }
    }

    public String getEnemyName(int i) {
        return enemies.get(i).getName();
    }

    public int getEnemyHealth(int i) {
        return enemies.get(i).getHealth();
    }

    public int getEnemyAttack(int i) {
        return enemies.get(i).getAttack();
    }

    public int getEnemyShield(int i) {
        return enemies.get(i).getShield();
    }

    public int pay(int i) {
        return enemies.get(i).getPay();
    }
}
