package io.github.aresgtr.elevator;

public class ElevatorStateHandler {

    private ElevatorState currentState;

    private boolean upOrDown;

    public ElevatorStateHandler() {
        this.currentState = new Idle();
    }

    public boolean getUpOrDown() {
        return upOrDown;
    }

    public void setUpOrDown(boolean upOrDown) {
        this.upOrDown = upOrDown;
    }





    public void setState(ElevatorState s) {
        currentState = s;
    }

    public void changeState() throws InterruptedException {
        currentState.changeState(this);
        while ((currentState instanceof GoUp) || (currentState instanceof GoDown)) {
            currentState.changeState(this);
        }
    }
}
