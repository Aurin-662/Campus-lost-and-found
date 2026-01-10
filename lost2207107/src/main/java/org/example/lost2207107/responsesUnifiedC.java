package org.example.lost2207107;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class responsesUnifiedC {

    @FXML private TableView<Object> reportsTable;
    @FXML private TableColumn<Object, Integer> idCol;
    @FXML private TableColumn<Object, String> itemCol;
    @FXML private TableColumn<Object, Integer> reporterCol;
    @FXML private TableColumn<Object, String> statusCol;

    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private int userId;

    @FXML
    public void initialize() {
        userId = loginC.getLoggedInUserId();

        idCol.setCellValueFactory(data -> {
            Object row = data.getValue();
            if (row instanceof ReportRow r) {
                return new SimpleIntegerProperty(r.getId()).asObject();
            } else if (row instanceof NotificationRow n) {
                return new SimpleIntegerProperty(n.getReportId()).asObject();
            }
            return null;
        });

        itemCol.setCellValueFactory(data -> {
            Object row = data.getValue();
            if (row instanceof ReportRow r) {
                return new SimpleStringProperty(r.getItemInfo());
            } else if (row instanceof NotificationRow n) {
                return new SimpleStringProperty(n.getItemInfo());
            }
            return null;
        });

        reporterCol.setCellValueFactory(data -> {
            Object row = data.getValue();
            if (row instanceof ReportRow r) {
                return new SimpleIntegerProperty(r.getReporterId()).asObject();
            }
            return null;
        });

        statusCol.setCellValueFactory(data -> {
            Object row = data.getValue();
            if (row instanceof ReportRow r) {
                return new SimpleStringProperty(r.getStatus());
            } else if (row instanceof NotificationRow n) {
                return new SimpleStringProperty(n.getStatus());
            }
            return null;
        });

        loadData();
    }

    // 🔁 Load both owner and reporter reports
    private void loadData() {
        reportsTable.getItems().clear();

        try {
            // Owner reports
            ResultSet ownerRs = DatabaseHelper.getReportsForOwner(userId);
            if (ownerRs != null) {
                while (ownerRs.next()) {
                    reportsTable.getItems().add(new ReportRow(
                            ownerRs.getInt("id"),
                            ownerRs.getInt("lost_item_id"),
                            ownerRs.getInt("found_item_id"),
                            ownerRs.getInt("reporter_id"),
                            ownerRs.getString("status"),
                            ownerRs.getString("lost_name"),
                            ownerRs.getString("lost_location"),
                            ownerRs.getString("lost_date"),
                            ownerRs.getString("found_name"),
                            ownerRs.getString("found_location"),
                            ownerRs.getString("found_date")
                    ));
                }
            }

            // Reporter reports
            ResultSet reporterRs = DatabaseHelper.getReportsByReporter(userId);
            if (reporterRs != null) {
                while (reporterRs.next()) {
                    reportsTable.getItems().add(new ReportRow(
                            reporterRs.getInt("id"),
                            reporterRs.getInt("lost_item_id"),
                            reporterRs.getInt("found_item_id"),
                            reporterRs.getInt("reporter_id"),
                            reporterRs.getString("status"),
                            reporterRs.getString("lost_name"),
                            reporterRs.getString("lost_location"),
                            reporterRs.getString("lost_date"),
                            reporterRs.getString("found_name"),
                            reporterRs.getString("found_location"),
                            reporterRs.getString("found_date")
                    ));
                }
            }

            // Buttons visible logic
            approveButton.setVisible(true);
            rejectButton.setVisible(true);
            deleteButton.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ OWNER ACTIONS
    @FXML
    private void approveSelected() {
        updateSelected("Approved");
    }

    @FXML
    private void rejectSelected() {
        updateSelected("Rejected");
    }

    private void updateSelected(String status) {
        Object selected = reportsTable.getSelectionModel().getSelectedItem();
        if (!(selected instanceof ReportRow r)) return;

        // 🔒 Check ownership before approving/rejecting
        boolean isOwner = false;
        if (r.getLostItemId() > 0) {
            int ownerId = DatabaseHelper.getOwnerIdForLostItem(r.getLostItemId());
            isOwner = (ownerId == userId);
        } else if (r.getFoundItemId() > 0) {
            int ownerId = DatabaseHelper.getOwnerIdForFoundItem(r.getFoundItemId());
            isOwner = (ownerId == userId);
        }

        if (!isOwner) {
            new Alert(Alert.AlertType.ERROR,
                    "❌ You cannot approve/reject this report. Wait for the item owner.").showAndWait();
            return;
        }

        // ✅ Owner confirmed → proceed
        DatabaseHelper.updateReportStatus(r.getId(), status);
        DatabaseHelper.insertNotification(
                r.getReporterId(),
                r.getId(),
                "Your request has been " + status + " (Report ID " + r.getId() + ")"
        );
        loadData();
    }

    @FXML
    private void deleteSelected() {
        Object selected = reportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        int reportId = (selected instanceof ReportRow r)
                ? r.getId()
                : ((NotificationRow) selected).getReportId();

        DatabaseHelper.deleteNotificationByReportId(reportId);
        DatabaseHelper.deleteReport(reportId);
        loadData();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) reportsTable.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}