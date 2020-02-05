package io.github.aresgtr;

import io.github.aresgtr.elevator.ElevatorStateHandler;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here

        ElevatorStateHandler elevatorStateHandler = new ElevatorStateHandler();

        elevatorStateHandler.setUpOrDown(false);    //  state machine controller

        elevatorStateHandler.changeState();
    }
}
