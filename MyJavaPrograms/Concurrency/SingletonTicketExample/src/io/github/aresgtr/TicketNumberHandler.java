package io.github.aresgtr;

public class TicketNumberHandler {

    private long nextUniqueNumber = 1;

    public synchronized long getTicketNumber() {
        return nextUniqueNumber++;
    }
}
