package org.example.lost2207107;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;

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

    private String selectedImagePath = null;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> {
            String itemName = safe(itemNameField.getText());
            String description = safe(descriptionArea.getText());
            String location = safe(locationField.getText());
            String date = (datePicker.getValue() != null) ? datePicker.getValue().toString() : "";
            String contact = safe(contactInfoField.getText());

            // Save to database
            boolean success = DatabaseHelper.insertLostItem(
                    1, // TODO: replace with actual logged-in user id
                    itemName,
                    description,
                    date,
                    location,
                    contact,
                    selectedImagePath
            );

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Lost Item Submitted");
                alert.setContentText("Your lost item has been recorded.");
                alert.showAndWait();

                // Show in view scene
                showViewScene(itemName, description, location, date, contact, selectedImagePath);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Submission Failed");
                alert.setContentText("Could not save lost item. Try again.");
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Cancel");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Your data will be discarded.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                loadReportScene();
            }
        });

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

    private void showViewScene(String itemName, String description, String location,
                               String date, String contact, String imagePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            Parent root = loader.load();

            viewC controller = loader.getController();
            controller.addLostItem(itemName, description, location, date, contact, imagePath);

            Stage stage = (Stage) submitButton.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/light.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadReportScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("report.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}