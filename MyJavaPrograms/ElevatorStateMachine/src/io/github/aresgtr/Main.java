package io.github.aresgtr;

import io.github.aresgtr.elevator.ElevatorStateHandler;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here

        ElevatorStateHandler elevatorStateHandler = new ElevatorStateHandler();

        elevatorStateHandler.pressFloorButton(3);   //  cannot be 0
        elevatorStateHandler.pressFloorButton(5);
        elevatorStateHandler.runElevator();
    }
}
