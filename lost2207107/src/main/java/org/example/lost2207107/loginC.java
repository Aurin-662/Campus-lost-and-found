package org.example.lost2207107;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class loginC {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;


    @FXML
    public void submit1(ActionEvent event) {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("⚠ Username and password required!");
            return;
        }

        if (DatabaseHelper.validateUser(user, pass)) {
            statusLabel.setText(" Login successful!");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("items.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(root);

                stage.setScene(scene);
            } catch (IOException e) {
                statusLabel.setText(" Failed to load items page.");
                e.printStackTrace();
            }
        } else {
            statusLabel.setText(" Invalid username or password.");
        }
    }


    @FXML
    public void sign(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
        } catch (IOException e) {
            statusLabel.setText("⚠ Failed to load signup page.");
            e.printStackTrace();
        }
    }
}