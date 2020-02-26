package io.github.aresgtr.elevator;

public class ElevatorStateHandler {

    private ElevatorState currentState;

    private int currentFloor;
    private int targetFloor;

    private char upOrDownStatus;


    public ElevatorStateHandler() {
        this.currentState = new Idle();
        this.currentFloor = 1;
    }


    /**
     * Getter and Setter
     */

    public char getUpOrDownStatus() {
        return upOrDownStatus;
    }


    public void setTargetFloor(int targetFloorNumber) throws InterruptedException {
        this.targetFloor = targetFloorNumber;
        if (targetFloorNumber > this.currentFloor) {
            this.upOrDownStatus = 'U';
        } else if (targetFloorNumber < this.currentFloor) {
            this.upOrDownStatus = 'D';
        } else {
            this.upOrDownStatus = 'N';
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
