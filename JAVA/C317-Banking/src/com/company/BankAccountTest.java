package com.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;

class BankAccountTest {

    private BankAccount account;
    private static int count;

    @org.junit.jupiter.api.BeforeAll
    static void beforeClass() {
        System.out.println("This executes before any test cases. Count = " + count++);
    }

    @org.junit.jupiter.api.BeforeEach
    void setup() {
        account = new BankAccount("Tim", "Buchalka", 1000.00, BankAccount.CHECKING);
        System.out.println("Running a test...");
    }

    @org.junit.jupiter.api.Test
    void deposit() {
        double balance = account.deposit(200.00, true);
        assertEquals(1200.00, balance, 0);
    }

    @org.junit.jupiter.api.Test
    void withdraw_branch() {
        double balance = account.withdraw(200.00, true);
        assertEquals(800.00, balance, 0);
    }

    @org.junit.jupiter.api.Test
    void withdraw_notBranch() {
//        assertEquals(400.00, balance, 0);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            double balance = account.withdraw(600.00, false);
        });
    }

    @org.junit.jupiter.api.Test
    void getBalance_deposit() {
        account.deposit(200.00, true);
        assertEquals(1200.00, account.getBalance(), 0);
    }

    @org.junit.jupiter.api.Test
    void getBalance_withdraw() {
        account.withdraw(200.00, true);
        assertEquals(800.00, account.getBalance(), 0);
    }

    @org.junit.jupiter.api.Test
    void isChecking_true() {
//        assertEquals(true, account.isChecking());
        assertTrue(account.isChecking());
    }

    @org.junit.jupiter.api.AfterAll
    static void afterClass() {
        System.out.println("This executes after any test cases. Count = " + count++);
    }

    @org.junit.jupiter.api.AfterEach
    void teardown() {
        System.out.println("Count = " + count++);
    }


//    @org.junit.jupiter.api.Test
//    public void dummyTest() {
//        assertEquals(20, 21);
//    }

}