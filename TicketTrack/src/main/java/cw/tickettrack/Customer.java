package cw.tickettrack;

import cw.tickettrack.Ticket;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrieveRate;
    private final int totalTickets;

    public Customer(TicketPool ticketPool, int retrieveRate, int totalTickets) {
        this.ticketPool = ticketPool;
        this.retrieveRate = retrieveRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalTickets; i++) {
            Ticket ticket = ticketPool.buyTicket();
            if (ticket != null) {
                System.out.println("Bought ticket: " + ticket);
            }
            try {
                Thread.sleep(retrieveRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
