package com.company;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {
	// write your code here
        final BankAccount account = new BankAccount("12345-678", 1000.00);

        //  Solution 1
//        Thread trThread1 = new Thread() {
//            public void run() {
//                account.deposit(300.00);
//                account.withdraw(50.00);
//            }
//        };
//
//        Thread trThread2 = new Thread() {
//            public void run() {
//                account.deposit(203.75);
//                account.withdraw(100.00);
//            }
//        };



        //  Solution 2
        Thread trThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                account.deposit(300.00);
                account.withdraw(50.00);
            }
        });

        Thread trThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                account.deposit(203.75);
                account.withdraw(100.00);
            }
        });

        trThread1.start();
        trThread2.start();
    }
















    static class BankAccount {

        private double balance;
        private String accountNumber;
        private Lock lock;

        public BankAccount(String accountNumber, double balance) {
            this.balance = balance;
            this.accountNumber = accountNumber;
            this.lock = new ReentrantLock();
        }

        public void deposit(double amount) {    //  add a trylock with timeout
            try {
                if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                    try {
                        balance += amount;
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Could not get the lock");
                }
            } catch (InterruptedException e){

            }
        }

        public void withdraw(double amount) {
            lock.lock();
            try {
                balance -= amount;
            } finally {
                lock.unlock();
            }
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void printAccountNumber() {
            System.out.println("Account number = " + accountNumber);
        }
    }
}
