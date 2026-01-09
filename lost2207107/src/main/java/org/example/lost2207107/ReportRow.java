package org.example.lost2207107;

public class ReportRow {
    private int id;
    private int lostItemId;
    private int foundItemId;
    private int reporterId;
    private String status;

    // Extra fields for item details
    private String lostName;
    private String lostLocation;
    private String lostDate;
    private String foundName;
    private String foundLocation;
    private String foundDate;

    public ReportRow(int id, int lostItemId, int foundItemId, int reporterId, String status,
                     String lostName, String lostLocation, String lostDate,
                     String foundName, String foundLocation, String foundDate) {
        this.id = id;
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.reporterId = reporterId;
        this.status = status;
        this.lostName = lostName;
        this.lostLocation = lostLocation;
        this.lostDate = lostDate;
        this.foundName = foundName;
        this.foundLocation = foundLocation;
        this.foundDate = foundDate;
    }

    public int getId() { return id; }
    public int getLostItemId() { return lostItemId; }
    public int getFoundItemId() { return foundItemId; }
    public int getReporterId() { return reporterId; }

    // ✅ Map Approved → Claimed for reporter view
    public String getStatus() {
        if ("Approved".equalsIgnoreCase(status)) {
            return "Claimed";
        }
        return status;
    }

    public String getLostName() { return lostName; }
    public String getLostLocation() { return lostLocation; }
    public String getLostDate() { return lostDate; }

    public String getFoundName() { return foundName; }
    public String getFoundLocation() { return foundLocation; }
    public String getFoundDate() { return foundDate; }

    // Helper method to show item info nicely
    public String getItemInfo() {
        if (lostItemId > 0) {
            return "Lost: " + lostName + " @ " + lostLocation + " (" + lostDate + ")";
        } else {
            return "Found: " + foundName + " @ " + foundLocation + " (" + foundDate + ")";
        }
    }
}