package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String privateVar = "this is private to main()";

        ScopeCheck scopeInstance = new ScopeCheck();
        System.out.println("scopeInstance privateVar is " + scopeInstance.getPrivateVar());

        scopeInstance.timesTwo();
        System.out.println("***********************8");
        ScopeCheck.InnerClass innerClass = scopeInstance.new InnerClass();
        innerClass.timesTwo();

    }
}
