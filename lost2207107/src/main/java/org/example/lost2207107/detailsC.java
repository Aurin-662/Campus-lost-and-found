package org.example.lost2207107;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class detailsC {

    @FXML private ImageView itemImage;
    @FXML private Label itemNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label locationLabel;
    @FXML private Label dateLabel;
    @FXML private Label contactLabel;

    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private int itemId;
    private int itemUserId;     // owner id from DB
    private int currentUserId;  // logged-in user id from session
    private boolean isLostItem; // flag to know if this is lost or found item

    public void setData(int itemId, String itemName, String description, String location,
                        String date, String contact, String imagePath,
                        int itemUserId, int currentUserId, boolean isLostItem) {

        this.itemId = itemId;
        this.itemUserId = itemUserId;
        this.currentUserId = currentUserId;
        this.isLostItem = isLostItem;

        itemNameLabel.setText("Item: " + itemName);
        descriptionLabel.setText("Description: " + description);
        locationLabel.setText("Location: " + location);
        dateLabel.setText(isLostItem ? "Date Lost: " + date : "Date Found: " + date);
        contactLabel.setText("Contact: " + contact);

        if (imagePath != null && !imagePath.isBlank()) {
            itemImage.setImage(new Image(imagePath));
        }

        // Show/hide edit/delete buttons based on owner check
        boolean isOwner = (itemUserId == currentUserId);
        editButton.setVisible(isOwner);
        deleteButton.setVisible(isOwner);

        deleteButton.setOnAction(e -> deleteItem());
        editButton.setOnAction(e -> editItem());
    }

    private void deleteItem() {
        if (itemUserId != currentUserId) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Delete failed. You are not the owner.");
            alert.showAndWait();
            return;
        }

        boolean success;
        if (isLostItem) {
            success = DatabaseHelper.deleteLostItem(itemId, currentUserId);
        } else {
            success = DatabaseHelper.deleteFoundItem(itemId, currentUserId);
        }

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item deleted successfully.");
            alert.showAndWait();
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Delete failed.");
            alert.showAndWait();
        }
    }

    private void editItem() {
        if (itemUserId != currentUserId) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Edit failed. You are not the owner.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(isLostItem ? "lost.fxml" : "found.fxml"));
            Parent root = loader.load();

            if (isLostItem) {
                lostC controller = loader.getController();
                controller.setEditData(itemId,
                        itemNameLabel.getText().replace("Item: ", ""),
                        descriptionLabel.getText().replace("Description: ", ""),
                        locationLabel.getText().replace("Location: ", ""),
                        dateLabel.getText().replace("Date Lost: ", ""),
                        contactLabel.getText().replace("Contact: ", ""),
                        itemImage.getImage() != null ? itemImage.getImage().getUrl() : null
                );
            } else {
                foundC controller = loader.getController();
                controller.setEditData(itemId,
                        itemNameLabel.getText().replace("Item: ", ""),
                        descriptionLabel.getText().replace("Description: ", ""),
                        locationLabel.getText().replace("Location: ", ""),
                        dateLabel.getText().replace("Date Found: ", ""),
                        contactLabel.getText().replace("Contact: ", ""),
                        itemImage.getImage() != null ? itemImage.getImage().getUrl() : null
                );
            }

            Stage stage = new Stage();
            stage.setTitle("Edit Item");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open edit form.");
            alert.showAndWait();
        }
    }
}