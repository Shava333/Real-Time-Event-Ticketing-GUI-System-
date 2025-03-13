package cw.tickettrack;

import java.math.BigDecimal;

public class Vendor implements Runnable {
    private final int totalTickets;
    private final int releaseRate;
    private final TicketPool ticketPool;
    private final String eventName;
    private final int ticketPrice;

    public Vendor(int totalTickets, int releaseRate, TicketPool ticketPool, String eventName, int ticketPrice) {
        this.totalTickets = totalTickets;
        this.releaseRate = releaseRate;
        this.ticketPool = ticketPool;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalTickets; i++) {
            Ticket ticket = new Ticket(i + 1, eventName, BigDecimal.valueOf(ticketPrice));
            ticketPool.addTicket(ticket);
            System.out.println("Added ticket: " + ticket);
            try {
                Thread.sleep(releaseRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
