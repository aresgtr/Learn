package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Account timsAccount = new Account("TIm");
        timsAccount.deposit(1000);
        timsAccount.withdraw(500);
        timsAccount.withdraw(-200);
        timsAccount.deposit(-20);
        timsAccount.calculateBalance();

        System.out.println("Balance on account is " + timsAccount.getBalance());
    }
}
