package io.github.aresgtr;

import io.github.aresgtr.elevator.ElevatorStateHandler;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here

        ElevatorStateHandler elevatorStateHandler = new ElevatorStateHandler();

        elevatorStateHandler.setTargetFloor(10);    //  state machine controller

        elevatorStateHandler.setTargetFloor(-5);

    }
}
