package cw.tickettrack;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationForm {
    private String userType;
    private Stage previousStage;

    public RegistrationForm(String userType, Stage previousStage) {
        this.userType = userType;
        this.previousStage = previousStage;
    }

    public VBox createLayout() {
        Label headingLabel = new Label("Register as " + capitalize(userType));
        headingLabel.getStyleClass().add("tab-heading");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        VBox nameBox = new VBox(5, nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        VBox emailBox = new VBox(5, emailLabel, emailField);
        emailBox.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        VBox passwordBox = new VBox(5, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            if (validateInputs(nameField.getText(), emailField.getText(), passwordField.getText())) {
                if (isValidEmail(emailField.getText())) {
                    boolean registrationSuccess = registerUser(nameField.getText(), emailField.getText(), passwordField.getText());
                    if (registrationSuccess) {
                        showLoginForm();
                        ((Stage) registerButton.getScene().getWindow()).close();
                    } else {
                        showErrorAlert("Registration failed. Please try again.");
                    }
                } else {
                    showErrorAlert("Invalid email address. Please enter a valid email.");
                }
            } else {
                showErrorAlert("All fields must be filled. Please complete the form.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            previousStage.show();
            ((Stage) backButton.getScene().getWindow()).close();
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(headingLabel, nameBox, emailBox, passwordBox, registerButton, backButton);
        layout.getStyleClass().add("form-layout");
        layout.setAlignment(Pos.CENTER);

        return layout;
    }

    private boolean validateInputs(String name, String email, String password) {
        return !(name.isEmpty() || email.isEmpty() || password.isEmpty());
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }

    private boolean registerUser(String name, String email, String password) {
        return DBUtil.registerUser(name, email, password, userType);
    }

    private void showLoginForm() {
        Stage loginStage = new Stage();
        LoginForm loginForm = new LoginForm(previousStage);
        Scene loginScene = new Scene(loginForm.createLayout(), 400, 600);
        loginScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        loginStage.setScene(loginScene);
        loginStage.show();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
