package org.example.lost2207107;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class reportC {
    private Parent root;
    private Stage stage;
    private Scene scene;



    @FXML
    public void home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("items.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

   /* @FXML
    public void log(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }*/
    public void signup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void lost(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lost.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void found(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("found.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void vieww(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        root= loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/light.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private ToggleButton darkModeToggle1;

    @FXML
    private BorderPane root2;
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            darkModeToggle1.setOnAction(event -> {
                Scene scene = root2.getScene();
                if (darkModeToggle1.isSelected()) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
                    darkModeToggle1.setText("Light Mode");
                } else {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(getClass().getResource("/css/light.css").toExternalForm());
                    darkModeToggle1.setText("Dark Mode");
                }
            });
        });
    }
}
