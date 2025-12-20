package org.example.lost2207107;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class viewC {

    @FXML private Label welcomeLabel;
    @FXML private FlowPane lostItemsContainer;
    @FXML private FlowPane foundItemsContainer;

    public void addLostItem(String itemName, String description, String location,
                            String date, String contact, String imagePath) {
        VBox card = createCard(itemName, description, location, date, contact, imagePath);
        lostItemsContainer.getChildren().add(card);
    }

    public void addFoundItem(String itemName, String description, String location,
                             String date, String contact, String imagePath) {
        VBox card = createCard(itemName, description, location, date, contact, imagePath);
        foundItemsContainer.getChildren().add(card);
    }

    private void openDetailsPage(String itemName, String description, String location,
                                 String date, String contact, String imagePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("details.fxml"));
            Parent root = loader.load();

            detailsC controller = loader.getController();
            controller.setData(itemName, description, location, date, contact, imagePath);

            Stage stage = new Stage();
            stage.setTitle("Item Details");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VBox createCard(String itemName, String description, String location,
                            String date, String contact, String imagePath) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        if (imagePath != null && !imagePath.isBlank()) {
            ImageView imageView = new ImageView(new Image(imagePath, 120, 80, true, true));
            imageView.setFitWidth(120);
            imageView.setFitHeight(80);
            card.getChildren().add(imageView);
        }

        Label titleLabel = new Label("Item: " + fallback(itemName, "N/A"));
        titleLabel.getStyleClass().add("card-title");

        Label descLabel = new Label("Description: " + fallback(description, "N/A"));
        descLabel.getStyleClass().add("card-label");
        descLabel.setWrapText(true);

        Label locLabel = new Label("Location: " + fallback(location, "N/A"));
        locLabel.getStyleClass().add("card-label");

        Label dateLabel = new Label("Date: " + fallback(date, "N/A"));
        dateLabel.getStyleClass().add("card-label");

        Label contactLabel = new Label("Contact: " + fallback(contact, "N/A"));
        contactLabel.getStyleClass().add("card-label");

        Button viewButton = new Button("View");
        viewButton.getStyleClass().add("primary-button");
        viewButton.setOnAction(e -> openDetailsPage(itemName, description, location, date, contact, imagePath));

        card.getChildren().addAll(titleLabel, descLabel, locLabel, dateLabel, contactLabel, viewButton);

        return card;
    }

    private String fallback(String s, String def) {
        return (s == null || s.isBlank()) ? def : s;
    }
}