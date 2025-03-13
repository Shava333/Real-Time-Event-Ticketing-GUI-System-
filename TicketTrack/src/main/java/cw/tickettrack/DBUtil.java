package cw.tickettrack;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/ticketsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Register a user (Customer or Vendor)
    public static boolean registerUser(String name, String email, String password, String userType) {
        String table = userType.equalsIgnoreCase("customer") ? "customer" : "vendor";
        String query = "INSERT INTO " + table + " (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login a user (Customer or Vendor)
    public static boolean loginUser(String email, String password, String userType) {
        String table = userType.equalsIgnoreCase("customer") ? "customer" : "vendor";
        String query = "SELECT * FROM " + table + " WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insert vendor inputs (for the VendorDashboard)
    public static boolean insertVendorInputs(int eventId, String eventName, int ticketPrice, int totalTickets, int ticketReleaseRate, int customerRetrieveRate, int maxCapacity) {
        String query = "INSERT INTO vendor_inputs (id, name, ticket_price, total_tickets, ticket_release_rate, customer_retrieval_rate, max_capacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            stmt.setString(2, eventName);
            stmt.setInt(3, ticketPrice);
            stmt.setInt(4, totalTickets);
            stmt.setInt(5, ticketReleaseRate);
            stmt.setInt(6, customerRetrieveRate);
            stmt.setInt(7, maxCapacity);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch ticket purchases for a customer
    public static ResultSet getTicketPurchases(int customerId) {
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

    // Add a ticket purchase record
    public static boolean addTicketPurchase(int customerId, int ticketId) {
        String query = "INSERT INTO ticket_purchases (customer_id, ticket_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, ticketId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
