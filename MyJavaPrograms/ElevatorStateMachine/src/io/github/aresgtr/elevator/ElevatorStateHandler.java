package io.github.aresgtr.elevator;

public class ElevatorStateHandler {

    private ElevatorState currentState;

    private int currentFloor;
    private int targetFloor;

    private char upOrDown;

    public ElevatorStateHandler() {
        this.currentState = new Idle();
        this.currentFloor = 1;
    }

    /**
     * Getter and Setter
     */

    public char getUpOrDown() {
        return upOrDown;
    }

    public void setTargetFloor(int targetFloorNumber) throws InterruptedException {
        this.targetFloor = targetFloorNumber;
        if (targetFloorNumber > this.currentFloor) {
            this.upOrDown = 'U';
        } else if (targetFloorNumber < this.currentFloor) {
            this.upOrDown = 'D';
        } else {
            this.upOrDown = 'N';
        }

        if (this.currentFloor != this.targetFloor) {
            changeState();
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor){
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
