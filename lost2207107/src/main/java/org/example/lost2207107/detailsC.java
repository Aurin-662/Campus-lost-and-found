package org.example.lost2207107;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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

        // TODO: open an edit form pre-filled with this item’s data
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Edit feature coming soon!");
        alert.showAndWait();
    }
}