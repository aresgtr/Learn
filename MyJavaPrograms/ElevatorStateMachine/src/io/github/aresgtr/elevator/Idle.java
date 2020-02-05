package io.github.aresgtr.elevator;

public class Idle implements ElevatorState {

    @Override
    public void changeState(ElevatorStateHandler handler) {

        if (handler.getUpOrDown() == 'U') {
            handler.setState(new GoUp());
            System.out.println("go up");
        } else if (handler.getUpOrDown() == 'D') {
            handler.setState(new GoDown());
            System.out.println("go down");
        } else {
            handler.setState(this);
            System.out.println("still idle, no change");
        }
    }
}
