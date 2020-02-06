package io.github.aresgtr.elevator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

public class ElevatorStateHandler {

    private ElevatorState currentState;

    private int currentFloor;
    private int targetFloor;

    private char upOrDown;

    private List<Integer> jobStack;

    public ElevatorStateHandler() {
        this.currentState = new Idle();
        this.currentFloor = 1;
        jobStack = new LinkedList<>();
    }


    /**
     * Getter and Setter
     */

    public char getUpOrDown() {
        return upOrDown;
    }

    public void pressFloorButton(int button) throws InterruptedException {
        jobStack.add(button);
        Collections.sort(jobStack);
    }

    public void runElevator() throws InterruptedException {
        while (!jobStack.isEmpty()) {
            System.out.println(jobStack.get(0));
            setTargetFloor(jobStack.get(0));
            jobStack.remove(0);
        }
    }

    private void setTargetFloor(int targetFloorNumber) throws InterruptedException {
        this.targetFloor = targetFloorNumber;
        if (targetFloorNumber > this.currentFloor) {
            this.upOrDown = 'U';
        } else if (targetFloorNumber < this.currentFloor) {
            this.upOrDown = 'D';
        } else {
            this.upOrDown = 'N';
        }

        System.out.println("Start from floor: " + this.currentFloor);
        changeState();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }


    /**
     * State Machine, No Touch
     */

    public void setState(ElevatorState s) {
        currentState = s;
    }

    public void changeState() throws InterruptedException {
        currentState.changeState(this);
        while ((currentState instanceof GoUp) || (currentState instanceof GoDown)) {
            currentState.changeState(this);
        }
        if (this.currentFloor != this.targetFloor) {
            changeState();
        }

    }
}
