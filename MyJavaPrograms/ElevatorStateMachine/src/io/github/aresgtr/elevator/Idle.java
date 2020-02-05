package io.github.aresgtr.elevator;

public class Idle implements ElevatorState {

    @Override
    public void changeState(ElevatorStateHandler handler) {

        if (handler.getUpOrDown()) {
            handler.setState(new GoUp());
            System.out.println("go up");
        } else {
            handler.setState(new GoDown());
            System.out.println("go down");
        }
    }
}
