package org.example.lost2207107;

public class NotificationRow {

    private int reportId;
    private String itemInfo;
    private String status;

    public NotificationRow(int reportId, String itemInfo, String status) {
        this.reportId = reportId;
        this.itemInfo = itemInfo;
        this.status = status;
    }

    public int getReportId() {
        return reportId;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public String getStatus() {
        return status;
    }
}
