package cw.tickettrack;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class VendorDashboard {
    private final Stage previousStage;
    private TicketPool ticketPool;
    private Thread vendorThread;
    private Thread customerThread;

    private boolean threadsRunning = false;
    private int remainingTickets;
    private TextArea logArea;

    public VendorDashboard(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public Scene createScene() {
        Label titleLabel = new Label("Vendor Dashboard");
        titleLabel.getStyleClass().add("tab-heading");

        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Event Name");
        eventNameField.getStyleClass().add("text-field");

        TextField ticketPriceField = new TextField();
        ticketPriceField.setPromptText("Ticket Price");
        ticketPriceField.getStyleClass().add("text-field");

        TextField totalTicketsField = new TextField();
        totalTicketsField.setPromptText("Total Tickets (Batch)");
        totalTicketsField.getStyleClass().add("text-field");

        TextField ticketReleaseRateField = new TextField();
        ticketReleaseRateField.setPromptText("Ticket Release Rate (seconds)");
        ticketReleaseRateField.getStyleClass().add("text-field");

        TextField customerRetrieveRateField = new TextField();
        customerRetrieveRateField.setPromptText("Customer Retrieve Rate (seconds)");
        customerRetrieveRateField.getStyleClass().add("text-field");

        TextField maxTicketCapacityField = new TextField();
        maxTicketCapacityField.setPromptText("Max Ticket Capacity");
        maxTicketCapacityField.getStyleClass().add("text-field");

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(300);
        logArea.setPrefWidth(400);
        logArea.getStyleClass().add("log-area");

        Button startButton = new Button("Start");
        startButton.getStyleClass().add("button");

        Button stopButton = new Button("Stop");
        stopButton.getStyleClass().add("button");

        Button addTicketButton = new Button("Add Ticket");
        addTicketButton.getStyleClass().add("button");

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button");

        startButton.setOnAction(e -> {
            if (threadsRunning) {
                showAlert(Alert.AlertType.ERROR, "Error", "Threads are already running.");
                return;
            }

            try {
                String eventName = eventNameField.getText();
                int ticketPrice = Integer.parseInt(ticketPriceField.getText());
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int ticketReleaseRate = Integer.parseInt(ticketReleaseRateField.getText());
                int customerRetrieveRate = Integer.parseInt(customerRetrieveRateField.getText());
                int maxCapacity = Integer.parseInt(maxTicketCapacityField.getText());

                if (eventName.isEmpty() || ticketPrice <= 0 || totalTickets <= 0 || ticketReleaseRate <= 0 || customerRetrieveRate <= 0 || maxCapacity <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "All inputs must be valid and non-empty.");
                    return;
                }

                ticketPool = new TicketPool(maxCapacity, this::appendToLog);
                remainingTickets = totalTickets;

                Vendor vendor = new Vendor(totalTickets, ticketReleaseRate, ticketPool, eventName, ticketPrice);
                Customer customer = new Customer(ticketPool, customerRetrieveRate, totalTickets);

                vendorThread = new Thread(vendor, "Vendor");
                customerThread = new Thread(customer, "Customer");

                logArea.clear();
                appendToLog("System started...");
                startThreads(vendorThread, customerThread);

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numeric values for all fields.");
            }
        });

        stopButton.setOnAction(e -> stopThreads());

        addTicketButton.setOnAction(e -> {
            if (remainingTickets > 0 && ticketPool.isBatchComplete()) {
                Ticket ticket = new Ticket(ticketPool.size() + 1, "Event", BigDecimal.valueOf(100.0));
                ticketPool.addTicket(ticket);
                remainingTickets--;
                appendToLog("Added ticket to pool. Remaining tickets: " + remainingTickets);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Cannot add tickets. Previous batch is still incomplete.");
            }
        });

        logoutButton.setOnAction(e -> {
            if (threadsRunning) {
                showAlert(Alert.AlertType.ERROR, "Error", "Stop the system before logging out.");
                return;
            }
            previousStage.show();
            ((Stage) logoutButton.getScene().getWindow()).close();
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(
                titleLabel, eventNameField, ticketPriceField, totalTicketsField,
                ticketReleaseRateField, customerRetrieveRateField, maxTicketCapacityField,
                startButton, stopButton, addTicketButton, logoutButton, logArea
        );
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("main-layout");

        Scene scene = new Scene(layout, 500, 700);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        return scene;
    }

    private void startThreads(Thread vendor, Thread customer) {
        vendor.start();
        customer.start();
        threadsRunning = true;

        new Thread(() -> {
            while (vendor.isAlive() || customer.isAlive()) {
                appendToLog("Vendor and Customer threads running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            threadsRunning = false;
            appendToLog("Threads stopped.");
        }).start();
    }

    private void stopThreads() {
        if (vendorThread != null && customerThread != null) {
            vendorThread.interrupt();
            customerThread.interrupt();
            threadsRunning = false;
            appendToLog("Threads stopped.");
        }
    }

    private void appendToLog(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
