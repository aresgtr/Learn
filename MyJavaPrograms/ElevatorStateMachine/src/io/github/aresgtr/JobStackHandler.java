package io.github.aresgtr;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.github.aresgtr.elevator.ElevatorStateHandler;

public class JobStackHandler {

    ElevatorStateHandler elevatorStateHandler;

    private List<Integer> jobStack;

    public JobStackHandler() {
        this.elevatorStateHandler = new ElevatorStateHandler();
        this.jobStack = new LinkedList<>();
    }

    public void pressFloorButton(int... buttons){
        for (int button : buttons) {
            jobStack.add(button);
        }
        Collections.sort(jobStack);
    }

    //  TODO: add up and down boolean buttons
    public void callElevatorFromFloor(int floorNumber) {
        jobStack.add(floorNumber);
        Collections.sort(jobStack);
    }

    public void runElevator() throws InterruptedException {
        while (!jobStack.isEmpty()) {
            System.out.println(">>>>>Target: " + jobStack.get(0));
            elevatorStateHandler.setTargetFloor(jobStack.get(0));
            jobStack.remove(0);
        }
    }
}
