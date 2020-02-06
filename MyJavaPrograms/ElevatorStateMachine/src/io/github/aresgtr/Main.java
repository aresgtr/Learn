package io.github.aresgtr;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here

        JobStackHandler handler = new JobStackHandler();

        handler.pressFloorButton(3, 5, 8, -1, 10);   //  cannot be 0
        handler.runElevator();
    }
}
