package cw.tickettrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketPurchaseDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/ticketsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Add a ticket purchase record to the database
    public static boolean addTicketPurchase(int customerId, int ticketId) {
        String query = "INSERT INTO ticket_purchases (customer_id, ticket_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, ticketId);
            stmt.executeUpdate();
            System.out.println("Ticket purchase added: Customer ID = " + customerId + ", Ticket ID = " + ticketId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve ticket purchases for a specific customer
    public static ResultSet getTicketPurchasesByCustomer(int customerId) {
        String query = "SELECT * FROM ticket_purchases WHERE customer_id = ?";
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve all ticket purchases
    public static ResultSet getAllTicketPurchases() {
        String query = "SELECT * FROM ticket_purchases";
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

