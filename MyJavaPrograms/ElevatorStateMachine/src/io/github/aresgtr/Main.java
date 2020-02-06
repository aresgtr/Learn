package io.github.aresgtr;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here

        JobStackHandler handler = new JobStackHandler();

        handler.pressFloorButton(5, 10);   //  cannot be 0
        handler.callElevatorFromFloor(3);
        handler.runElevator();
    }
}
