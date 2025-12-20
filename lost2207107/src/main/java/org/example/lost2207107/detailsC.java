package org.example.lost2207107;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class detailsC {

    @FXML private ImageView itemImage;
    @FXML private Label itemNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label locationLabel;
    @FXML private Label dateLabel;
    @FXML private Label contactLabel;

    public void setData(String itemName, String description, String location,
                        String date, String contact, String imagePath) {
        itemNameLabel.setText("Item: " + itemName);
        descriptionLabel.setText("Description: " + description);
        locationLabel.setText("Location: " + location);
        dateLabel.setText("Date Lost: " + date);
        contactLabel.setText("Contact: " + contact);

        if (imagePath != null && !imagePath.isBlank()) {
            itemImage.setImage(new Image(imagePath));
        }
    }
}