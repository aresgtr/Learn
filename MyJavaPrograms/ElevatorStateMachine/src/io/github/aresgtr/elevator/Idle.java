package io.github.aresgtr.elevator;

public class Idle implements ElevatorState {

    @Override
    public void changeState(ElevatorStateHandler handler) {

        if (handler.getUpOrDownStatus() == 'U') {
            handler.setState(new GoUp());
        } else if (handler.getUpOrDownStatus() == 'D') {
            handler.setState(new GoDown());
        } else {
            handler.setState(this);
            System.out.println("still idle, no change");
        }
    }
}
