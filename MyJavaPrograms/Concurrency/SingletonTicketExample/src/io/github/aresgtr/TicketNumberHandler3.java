package io.github.aresgtr;

public class TicketNumberHandler3 extends TicketNumberHandler {

    private static TicketNumberHandler3 INSTANCE = new TicketNumberHandler3();

    private TicketNumberHandler3(){};

    public static TicketNumberHandler3 getInstance() {
        if (INSTANCE == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            INSTANCE = new TicketNumberHandler3();
        }
        return INSTANCE;
    }
}
