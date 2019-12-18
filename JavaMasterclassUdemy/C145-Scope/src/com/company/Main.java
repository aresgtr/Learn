package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String varFour = "this is private to main()";

        ScopeCheck scopeInstance = new ScopeCheck();
        System.out.println("scopeInstance varOne is " + scopeInstance.getVarOne());

        scopeInstance.timesTwo();
        System.out.println("***********************8");
        ScopeCheck.InnerClass innerClass = scopeInstance.new InnerClass();
        innerClass.timesTwo();

    }
}
