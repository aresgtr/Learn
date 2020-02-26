package io.github.aresgtr.elevator;

public interface ElevatorState {

    void changeState(ElevatorStateHandler handler) throws InterruptedException;
}
