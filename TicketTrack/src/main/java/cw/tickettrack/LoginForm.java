package cw.tickettrack;

import javafx.geometry.Pos;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginForm {
    private final Stage previousStage;

    public LoginForm(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public VBox createLayout() {
        Label headingLabel = new Label("Login");
        headingLabel.getStyleClass().add("tab-heading");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        VBox emailBox = new VBox(5, emailLabel, emailField);
        emailBox.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        VBox passwordBox = new VBox(5, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        Label userTypeLabel = new Label("User Type:");
        ChoiceBox<String> userTypeChoiceBox = new ChoiceBox<>();
        userTypeChoiceBox.getItems().addAll("Customer", "Vendor");
        userTypeChoiceBox.setValue("Customer");
        VBox userTypeBox = new VBox(5, userTypeLabel, userTypeChoiceBox);
        userTypeBox.setAlignment(Pos.CENTER_LEFT);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String userType = userTypeChoiceBox.getValue().toLowerCase();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Email and password cannot be empty.");
                return;
            }

            if (DBUtil.loginUser(email, password, userType)) {
                if (userType.equals("customer")) {
                    redirectToCustomerDashboard();
                } else if (userType.equals("vendor")) {
                    redirectToVendorDashboard();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid login credentials. Please try again.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            previousStage.show();
            ((Stage) backButton.getScene().getWindow()).close();
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(headingLabel, emailBox, passwordBox, userTypeBox, loginButton, backButton);
        layout.getStyleClass().add("form-layout");
        layout.setAlignment(Pos.CENTER);

        return layout;
    }

    private void redirectToCustomerDashboard() {
        Stage customerStage = new Stage();
        CustomerDashboard customerDashboard = new CustomerDashboard(previousStage);
        customerStage.setScene(customerDashboard.createScene());
        customerStage.setTitle("Customer Dashboard");
        customerStage.show();
        closeCurrentWindow();
    }

    private void redirectToVendorDashboard() {
        Stage vendorStage = new Stage();
        VendorDashboard vendorDashboard = new VendorDashboard(previousStage);
        vendorStage.setScene(vendorDashboard.createScene());
        vendorStage.setTitle("Vendor Dashboard");
        vendorStage.show();
        closeCurrentWindow();
    }

    private void closeCurrentWindow() {
        Stage currentStage = (Stage) previousStage.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
