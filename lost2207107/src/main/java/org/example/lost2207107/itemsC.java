package org.example.lost2207107;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; import javafx.scene.Node;
import javafx.scene.Parent; import javafx.scene.Scene;
import javafx.scene.control.ToggleButton; import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class itemsC {
    private Parent root1;
    private Stage stage;
    private Scene scene;

    @FXML
    public void report(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("report.fxml"));
        root1 = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root1);
        stage.setScene(scene);
        stage.show();
    }
    /*@FXML
    public void log1(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        root1 = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root1);
        stage.setScene(scene);
        stage.show();
    }*/
    @FXML
    public void vieww(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        root1 = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root1);
        stage.setScene(scene);
        stage.show();
    }
    public void signup1(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
        root1 = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root1);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        root1 = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root1);
        stage.setScene(scene);
        stage.show();
    }

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