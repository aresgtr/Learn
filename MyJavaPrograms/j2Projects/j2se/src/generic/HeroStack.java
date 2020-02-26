package generic;

import java.util.LinkedList;

import charactor.Hero;

public class HeroStack {

    LinkedList<Hero> heroes = new LinkedList<>();

    public void push(Hero h) {
        heroes.addLast(h);
    }

    public Hero pull() {
        return heroes.removeLast();
    }

    public Hero peek() {
        return heroes.getLast();
    }
}
