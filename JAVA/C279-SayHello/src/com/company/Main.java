package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        PolitePerson jane = new PolitePerson("Jane");
        PolitePerson john = new PolitePerson("John");

//        jane.sayHello(john);
//
//        System.out.println("=================================");

        new Thread(new Runnable() {
            @Override
            public void run() {
                jane.sayHello(john);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                john.sayHello(jane);
            }
        }).start();
    }


    /*
        1.  Thread1 acquires the lock on the jane object and enters the sayHello() method.
            It prints to the console, then suspends.
        2.  Thread2 acquires the lock on the john object and enters the sayHello() method.
            It prints to the console, then suspends.
        3.  Thread1 runs again and wants to say hello back to the john object. It tries to call the sayHelloBack() method
            using the john object that ws passed into the sayHello() method,
            but Thread2 is holding the john lock, so Thread1 suspends.
        4.  Thread2 runs again and wants to say hello back to the jane object. It tries to call the sayHelloBack() method
            using the jane object that ws passed into the sayHello() method,
            but Thread1 is holding the jane lock, so Thread1 suspends.
     */



    static class PolitePerson {
        private final String name;

        public PolitePerson(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public synchronized void sayHello(PolitePerson person) {
            System.out.format("%s: %s" + " has say hello to me!%n", this.name, person.getName());
            person.sayHelloBack(this);
        }

        public synchronized void sayHelloBack(PolitePerson person) {
            System.out.format("%s: %s" + " has said hello back to me!%n", this.name, person.getName());
        }
    }
}
