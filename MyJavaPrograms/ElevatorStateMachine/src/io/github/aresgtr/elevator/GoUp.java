package io.github.aresgtr.elevator;

public class GoUp implements ElevatorState {
    @Override
    public void changeState(ElevatorStateHandler handler) throws InterruptedException {
        handler.setState(new Idle());
        System.out.println("go idle");
    }
}
