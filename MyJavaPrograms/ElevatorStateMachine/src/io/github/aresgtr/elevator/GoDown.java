package io.github.aresgtr.elevator;

public class GoDown implements ElevatorState {

    @Override
    public void changeState(ElevatorStateHandler handler) throws InterruptedException {
        handler.setCurrentFloor(handler.getCurrentFloor() - 1);
        if (handler.getCurrentFloor() == 0) {
            handler.setCurrentFloor(-1);
        }
        handler.setState(new Idle());
        System.out.println("go idle, now at floor: " + handler.getCurrentFloor());
    }
}
