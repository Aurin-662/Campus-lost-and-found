package org.example.lost2207107;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class lostC {

    @FXML private TextField itemNameField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField locationField;
    @FXML private DatePicker datePicker;
    @FXML private TextField contactInfoField;

    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    @FXML private Button browseButton;

    @FXML private FlowPane thumbnailContainer;

    // store selected image path
    private String selectedImagePath = null;

    @FXML
    public void initialize() {
        // Submit button → collect data and show view page
        submitButton.setOnAction(event -> {
            String itemName = safe(itemNameField.getText());
            String description = safe(descriptionArea.getText());
            String location = safe(locationField.getText());
            String date = (datePicker.getValue() != null) ? datePicker.getValue().toString() : "";
            String contact = safe(contactInfoField.getText());

            showViewScene(itemName, description, location, date, contact, selectedImagePath);
        });

        // Cancel button → clear all fields
        cancelButton.setOnAction(e -> {
            itemNameField.clear();
            descriptionArea.clear();
            locationField.clear();
            datePicker.setValue(null);
            contactInfoField.clear();
            thumbnailContainer.getChildren().clear();
            selectedImagePath = null;
        });

        // Browse button → open FileChooser and show thumbnails
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(browseButton.getScene().getWindow());
            if (file != null) {
                selectedImagePath = file.toURI().toString();
                Image image = new Image(selectedImagePath, 100, 100, true, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                thumbnailContainer.getChildren().clear(); // show only latest
                thumbnailContainer.getChildren().add(imageView);
            }
        });
    }

    // Switch to view.fxml and pass data
    private void showViewScene(String itemName, String description, String location,
                               String date, String contact, String imagePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            Parent root = loader.load();

            viewC controller = loader.getController();
            // now pass imagePath too
            controller.addLostItem(itemName, description, location, date, contact, imagePath);

            Stage stage = (Stage) submitButton.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            // Default theme load (light.css)
            scene.getStylesheets().add(getClass().getResource("/css/light.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}