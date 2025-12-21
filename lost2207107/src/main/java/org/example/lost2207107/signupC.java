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

public class signupC {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;


    @FXML
    public void submit2(ActionEvent event) {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText(" Username and password required!");
            return;
        }

        boolean success = DatabaseHelper.insertUser(user, pass);
        if (success) {
            statusLabel.setText(" Signup successful!");
            openLoginScene(event);
        } else {
            statusLabel.setText(" Signup failed. Try another username.");
        }
    }


    @FXML
    public void login(ActionEvent event) {
        openLoginScene(event);
    }


    private void openLoginScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
        } catch (IOException e) {
            statusLabel.setText(" Failed to load login page.");
            e.printStackTrace();
        }
    }
}