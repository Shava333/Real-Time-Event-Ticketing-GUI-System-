package cw.tickettrack;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Real Time Ticketing Application");

        Label titleLabel = new Label("Real Time Ticketing Application");
        titleLabel.getStyleClass().add("main-title");

        Button customerButton = new Button("Register as Customer");
        Button vendorButton = new Button("Register as Vendor");
        Button loginButton = new Button("Login");

        customerButton.setOnAction(e -> showRegistrationPage("customer", primaryStage));
        vendorButton.setOnAction(e -> showRegistrationPage("vendor", primaryStage));
        loginButton.setOnAction(e -> showLoginPage(primaryStage));

        VBox layout = new VBox(30);
        layout.getChildren().addAll(titleLabel, customerButton, vendorButton, loginButton);
        layout.getStyleClass().add("main-layout");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showRegistrationPage(String userType, Stage previousStage) {
        previousStage.hide();

        Stage stage = new Stage();
        stage.setTitle("Register as " + capitalize(userType));

        RegistrationForm registrationForm = new RegistrationForm(userType, previousStage);
        Scene scene = new Scene(registrationForm.createLayout(), 400, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void showLoginPage(Stage previousStage) {
        previousStage.hide();

        Stage stage = new Stage();
        stage.setTitle("Login");

        LoginForm loginForm = new LoginForm(previousStage);
        Scene scene = new Scene(loginForm.createLayout(), 400, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
