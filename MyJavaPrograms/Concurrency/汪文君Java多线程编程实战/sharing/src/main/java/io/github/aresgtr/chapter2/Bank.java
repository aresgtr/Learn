package io.github.aresgtr.chapter2;

public class Bank {

    public static void main(String[] args) {

        TicketWindow ticketWindow1 = new TicketWindow("1");
        TicketWindow ticketWindow2 = new TicketWindow("2");
        TicketWindow ticketWindow3 = new TicketWindow("3");
        TicketWindow ticketWindow4 = new TicketWindow("4");

        ticketWindow1.start();
        ticketWindow2.start();
        ticketWindow3.start();
        ticketWindow4.start();
    }

}
