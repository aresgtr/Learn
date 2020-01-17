package io.github.aresgtr;

import java.util.Vector;

public class VectorTest {

    public static void main(String[] args) {

        int one = 1;
        int two = 2;
        String a = "a";
        char c = 'c';

        Animal animal = new Animal("Lala", 12);

        Vector<Object> v1 = new Vector<>();
        v1.addElement(one);
        v1.addElement(two);
        v1.addElement(a);
        v1.addElement(c);
        v1.addElement(animal);

        System.out.println(v1.indexOf(animal));

    }
}

class Animal {
    String name;
    int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
