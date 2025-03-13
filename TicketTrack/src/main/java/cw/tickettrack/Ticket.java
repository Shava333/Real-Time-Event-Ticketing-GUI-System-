package cw.tickettrack;

import java.math.BigDecimal;

public class Ticket {
    private int ticketId;
    private String eventName;
    private BigDecimal ticketPrice;

    public Ticket(int ticketId, String eventName, BigDecimal ticketPrice) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
    }

    public String getEventName() {
        return eventName;
    }

    public int getTicketId() {
        return ticketId;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", eventName='" + eventName + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}

