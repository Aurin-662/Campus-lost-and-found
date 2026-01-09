package org.example.lost2207107;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class responsesC {

    @FXML private TableView<ReportRow> reportsTable;
    @FXML private TableColumn<ReportRow, Integer> idCol;
    @FXML private TableColumn<ReportRow, String> itemCol;      // ✅ new column for item info
    @FXML private TableColumn<ReportRow, Integer> reporterCol;
    @FXML private TableColumn<ReportRow, String> statusCol;

    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button backButton;
    @FXML private Button deleteButton;

    private int ownerId;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        reporterCol.setCellValueFactory(new PropertyValueFactory<>("reporterId"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // ✅ Bind item info using ReportRow.getItemInfo()
        itemCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemInfo()));

        ownerId = loginC.getLoggedInUserId();
        loadReports();

        approveButton.setOnAction(e -> updateSelectedReport("Approved"));
        rejectButton.setOnAction(e -> updateSelectedReport("Rejected"));
        backButton.setOnAction(e -> goBack());   // Back button action
        deleteButton.setOnAction(e -> deleteSelectedNotification());
    }

    // ✅ Delete notification by reportId
    private void deleteSelectedNotification() {
        ReportRow selected = reportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No report selected.");
            alert.showAndWait();
            return;
        }

        boolean success = DatabaseHelper.deleteNotificationByReportId(selected.getId());
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Notification deleted for Report ID " + selected.getId());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Failed to delete notification.");
            alert.showAndWait();
        }
    }

    // ✅ Load reports for owner
    private void loadReports() {
        reportsTable.getItems().clear();
        ResultSet rs = DatabaseHelper.getReportsForOwner(ownerId);
        try {
            while (rs != null && rs.next()) {
                reportsTable.getItems().add(new ReportRow(
                        rs.getInt("id"),
                        rs.getInt("lost_item_id"),
                        rs.getInt("found_item_id"),
                        rs.getInt("reporter_id"),
                        rs.getString("status"),
                        rs.getString("lost_name"),
                        rs.getString("lost_location"),
                        rs.getString("lost_date"),
                        rs.getString("found_name"),
                        rs.getString("found_location"),
                        rs.getString("found_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Approve/Reject report and insert notification with reportId
    private void updateSelectedReport(String newStatus) {
        ReportRow selected = reportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No report selected.");
            alert.showAndWait();
            return;
        }

        boolean success = DatabaseHelper.updateReportStatus(selected.getId(), newStatus);
        if (success) {
            // Reporter side notification: insert/update message
            String msg;
            if ("Approved".equalsIgnoreCase(newStatus)) {
                msg = "Your claim/found request for item has been approved (Report ID " + selected.getId() + ")";
            } else {
                msg = "Your claim/found request for item has been rejected (Report ID " + selected.getId() + ")";
            }

            // ✅ Insert notification with reportId
            DatabaseHelper.insertNotification(selected.getReporterId(), selected.getId(), msg);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report updated to " + newStatus);
            alert.showAndWait();
            loadReports();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update report.");
            alert.showAndWait();
        }
    }

    // ✅ Back button method
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load view.fxml");
            alert.showAndWait();
        }
    }
}