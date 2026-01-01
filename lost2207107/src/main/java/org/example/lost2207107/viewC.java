package org.example.lost2207107;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class viewC {

    @FXML private BorderPane viewRoot;
    @FXML private Label welcomeLabel;
    @FXML private FlowPane lostItemsContainer;
    @FXML private FlowPane foundItemsContainer;

    @FXML private Button postItemBtn;
    @FXML private Button feedBtn;
    @FXML private Button responsesBtn;
    @FXML private Button signOutBtn;

    private static final String URL = "jdbc:sqlite:users.db";

    // Logged-in user id from login/session
    private int currentUserId = loginC.getLoggedInUserId();

    @FXML
    public void initialize() {
        // Navigation
        feedBtn.setOnAction(e -> loadScene("items.fxml"));
        responsesBtn.setOnAction(e -> loadScene("responses.fxml"));
        signOutBtn.setOnAction(e -> loadScene("signup.fxml"));

        // Always load items fresh from DB
        loadLostItems();
        loadFoundItems();
    }

    // Load lost items from DB
    private void loadLostItems() {
        lostItemsContainer.getChildren().clear();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM lost_items")) {

            while (rs.next()) {
                int itemId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String itemName = rs.getString("item_name");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String date = rs.getString("date_lost");
                String contact = rs.getString("contact");
                String imagePath = rs.getString("image_path");


                VBox card = createCard(itemId, userId, itemName, description, location, date, contact, imagePath, true);
                lostItemsContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load found items from DB
    private void loadFoundItems() {
        foundItemsContainer.getChildren().clear();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM found_items")) {

            while (rs.next()) {
                int itemId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String itemName = rs.getString("item_name");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String date = rs.getString("date_found");
                String contact = rs.getString("contact");
                String imagePath = rs.getString("image_path");


                VBox card = createCard(itemId, userId, itemName, description, location, date, contact, imagePath, false);
                foundItemsContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Open details page
    private void openDetailsPage(int itemId, int userId, String itemName, String description, String location,
                                 String date, String contact, String imagePath, boolean isLostItem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("details.fxml"));
            Parent root = loader.load();

            detailsC controller = loader.getController();

            controller.setData(itemId, itemName, description, location, date, contact, imagePath,
                    userId, currentUserId, isLostItem);

            Stage stage = new Stage();
            stage.setTitle("Item Details");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create card for item
    private VBox createCard(int itemId, int userId, String itemName, String description, String location,
                            String date, String contact, String imagePath, boolean isLostItem) {
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

        Label dateLabel = new Label((isLostItem ? "Date Lost: " : "Date Found: ") + fallback(date, "N/A"));
        dateLabel.getStyleClass().add("card-label");

        Label contactLabel = new Label("Contact: " + fallback(contact, "N/A"));
        contactLabel.getStyleClass().add("card-label");

        Button viewButton = new Button("View");
        viewButton.getStyleClass().add("primary-button");
        viewButton.setOnAction(e -> openDetailsPage(itemId, userId, itemName, description, location,
                date, contact, imagePath, isLostItem));

        card.getChildren().addAll(titleLabel, descLabel, locLabel, dateLabel, contactLabel, viewButton);

        return card;
    }

    private String fallback(String s, String def) {
        return (s == null || s.isBlank()) ? def : s;
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) viewRoot.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}