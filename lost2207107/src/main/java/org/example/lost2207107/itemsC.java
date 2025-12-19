package org.example.lost2207107;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

public class itemsC {

    @FXML
    private ToggleButton darkModeToggle;

    @FXML
    private BorderPane root;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            darkModeToggle.setOnAction(event -> {
                Scene scene = root.getScene();
                if (darkModeToggle.isSelected()) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
                    darkModeToggle.setText("Light Mode");
                } else {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(getClass().getResource("/css/light.css").toExternalForm());
                    darkModeToggle.setText("Dark Mode");
                }
            });
        });
    }
}