package com.company;

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

        public BankAccount(String accountNumber, double balance) {
            this.balance = balance;
            this.accountNumber = accountNumber;
        }

        public synchronized void deposit(double amount) {
            balance += amount;
        }

        public synchronized void withdraw(double amount) {
            balance -= amount;
        }
    }
}
