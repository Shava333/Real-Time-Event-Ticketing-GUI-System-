package cw.tickettrack;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class TicketPool {
    private final int maximumTicketCapacity;
    private final Queue<Ticket> tickets;
    private final Consumer<String> logCallback;

    public TicketPool(int maximumTicketCapacity, Consumer<String> logCallback) {
        this.maximumTicketCapacity = maximumTicketCapacity;
        this.tickets = new LinkedList<>();
        this.logCallback = logCallback;
    }

    public synchronized void addTicket(Ticket ticket) {
        while (tickets.size() >= maximumTicketCapacity) {
            try {
                logCallback.accept("Ticket pool full. Vendor is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while adding ticket.");
            }
        }
        tickets.add(ticket);
        logCallback.accept("Ticket added: " + ticket + " | Current size: " + tickets.size());
        notifyAll();
    }

    public synchronized Ticket buyTicket() {
        while (tickets.isEmpty()) {
            try {
                logCallback.accept("No tickets available. Customer is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while buying ticket.");
            }
        }
        Ticket ticket = tickets.poll();
        logCallback.accept("Ticket bought: " + ticket + " | Current size: " + tickets.size());
        notifyAll();
        return ticket;
    }

    public synchronized int remainingCapacity() {
        return maximumTicketCapacity - tickets.size();
    }

    public synchronized boolean isBatchComplete() {
        return tickets.isEmpty();
    }

    public synchronized int size() {
        return tickets.size();
    }
}
