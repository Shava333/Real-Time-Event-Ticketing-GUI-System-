package cw.tickettrack;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerDashboard {
    private final Stage previousStage;

    public CustomerDashboard(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public Scene createScene() {
        Label titleLabel = new Label("Customer Dashboard");
        titleLabel.getStyleClass().add("tab-heading");

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button");
        logoutButton.setOnAction(e -> {
            previousStage.show();
            ((Stage) logoutButton.getScene().getWindow()).close();
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(titleLabel, logoutButton);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("main-layout");

        Scene scene = new Scene(layout, 400, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        return scene;
    }
}
