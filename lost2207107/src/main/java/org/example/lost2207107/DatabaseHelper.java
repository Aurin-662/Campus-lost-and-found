package org.example.lost2207107;

import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:users.db";

    static {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Users table
            String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL)";
            stmt.execute(sqlUsers);

            // Lost items table
            String sqlLost = "CREATE TABLE IF NOT EXISTS lost_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "item_name TEXT NOT NULL," +
                    "description TEXT," +
                    "date_lost TEXT," +
                    "location TEXT," +
                    "contact TEXT," +
                    "image_path TEXT," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            stmt.execute(sqlLost);

            // Found items table
            String sqlFound = "CREATE TABLE IF NOT EXISTS found_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "item_name TEXT NOT NULL," +
                    "description TEXT," +
                    "date_found TEXT," +
                    "location TEXT," +
                    "contact TEXT," +
                    "image_path TEXT," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            stmt.execute(sqlFound);

            // Reports table for Found/Claim workflow
            String sqlReports = "CREATE TABLE IF NOT EXISTS reports (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "lost_item_id INTEGER," +
                    "found_item_id INTEGER," +
                    "reporter_id INTEGER NOT NULL," +
                    "status TEXT DEFAULT 'Pending'," +
                    "FOREIGN KEY(lost_item_id) REFERENCES lost_items(id)," +
                    "FOREIGN KEY(found_item_id) REFERENCES found_items(id)," +
                    "FOREIGN KEY(reporter_id) REFERENCES users(id))";
            stmt.execute(sqlReports);
            // Notifications table
            String sqlNotifications = "CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "report_id INTEGER NOT NULL, " +
                    "message TEXT NOT NULL, " +
                    "timestamp TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            stmt.execute(sqlNotifications);

            System.out.println("✅ Database initialized with owner-based tables.");

        } catch (SQLException e) {
            System.err.println("Database initialization failed:");
            e.printStackTrace();
        }
    }

    // Insert new user
    public static boolean insertUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Signup failed: " + e.getMessage());
            return false;
        }
    }

    // Validate user
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Login validation failed: " + e.getMessage());
            return false;
        }
    }

    // Get user ID by username
    public static int getUserId(String username) {
        String sql = "SELECT id FROM users WHERE username=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("User ID fetch failed: " + e.getMessage());
        }
        return -1; // fallback if not found
    }

    // Insert lost item
    public static boolean insertLostItem(int userId, String itemName, String description,
                                         String dateLost, String location, String contact, String imagePath) {
        String sql = "INSERT INTO lost_items(user_id, item_name, description, date_lost, location, contact, image_path) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, itemName);
            pstmt.setString(3, description);
            pstmt.setString(4, dateLost);
            pstmt.setString(5, location);
            pstmt.setString(6, contact);
            pstmt.setString(7, imagePath);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lost item insert failed: " + e.getMessage());
            return false;
        }
    }

    // Insert found item
    public static boolean insertFoundItem(int userId, String itemName, String description,
                                          String dateFound, String location, String contact, String imagePath) {
        String sql = "INSERT INTO found_items(user_id, item_name, description, date_found, location, contact, image_path) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, itemName);
            pstmt.setString(3, description);
            pstmt.setString(4, dateFound);
            pstmt.setString(5, location);
            pstmt.setString(6, contact);
            pstmt.setString(7, imagePath);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Found item insert failed: " + e.getMessage());
            return false;
        }
    }

    // Update lost item (ownership enforced)
    public static boolean updateLostItem(int itemId, int userId, String itemName, String description,
                                         String dateLost, String location, String contact, String imagePath) {
        String sql = "UPDATE lost_items SET item_name=?, description=?, date_lost=?, location=?, contact=?, image_path=? WHERE id=? AND user_id=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemName);
            pstmt.setString(2, description);
            pstmt.setString(3, dateLost);
            pstmt.setString(4, location);
            pstmt.setString(5, contact);
            pstmt.setString(6, imagePath);
            pstmt.setInt(7, itemId);
            pstmt.setInt(8, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update lost item failed: " + e.getMessage());
            return false;
        }
    }

    //  Update found item (ownership enforced)
    public static boolean updateFoundItem(int itemId, int userId, String itemName, String description,
                                          String dateFound, String location, String contact, String imagePath) {
        String sql = "UPDATE found_items SET item_name=?, description=?, date_found=?, location=?, contact=?, image_path=? WHERE id=? AND user_id=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemName);
            pstmt.setString(2, description);
            pstmt.setString(3, dateFound);
            pstmt.setString(4, location);
            pstmt.setString(5, contact);
            pstmt.setString(6, imagePath);
            pstmt.setInt(7, itemId);
            pstmt.setInt(8, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update found item failed: " + e.getMessage());
            return false;
        }
    }

    // Delete lost item (ownership enforced)
    public static boolean deleteLostItem(int itemId, int userId) {
        String sql = "DELETE FROM lost_items WHERE id=? AND user_id=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete lost item failed: " + e.getMessage());
            return false;
        }
    }

    // Delete found item (ownership enforced)
    public static boolean deleteFoundItem(int itemId, int userId) {
        String sql = "DELETE FROM found_items WHERE id=? AND user_id=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete found item failed: " + e.getMessage());
            return false;
        }
    }

    // Create report entry for Found/Claim workflow
    public static boolean createReport(int lostItemId, int foundItemId, int reporterId) {
        String sql = "INSERT INTO reports(lost_item_id, found_item_id, reporter_id, status) VALUES(?, ?, ?, 'Pending')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (lostItemId > 0) pstmt.setInt(1, lostItemId);
            else pstmt.setNull(1, Types.INTEGER);

            if (foundItemId > 0) pstmt.setInt(2, foundItemId);
            else pstmt.setNull(2, Types.INTEGER);

            pstmt.setInt(3, reporterId);
            pstmt.executeUpdate();

            // 🔥 get generated report id
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int reportId = rs.getInt(1);

                // ✅ insert pending notification for reporter
                insertNotification(
                        reporterId,
                        reportId,
                        "Your claim/found request is pending approval (Report ID " + reportId + ")"
                );
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Report creation failed: " + e.getMessage());
            return false;
        }
    }


    // Get all reports for a given owner (lost or found items)
    public static ResultSet getReportsForOwner(int ownerId) {
        String sql = "SELECT r.id, r.lost_item_id, r.found_item_id, r.reporter_id, r.status, " +
                "l.item_name AS lost_name, l.location AS lost_location, l.date_lost AS lost_date, " +
                "f.item_name AS found_name, f.location AS found_location, f.date_found AS found_date " +
                "FROM reports r " +
                "LEFT JOIN lost_items l ON r.lost_item_id = l.id " +
                "LEFT JOIN found_items f ON r.found_item_id = f.id " +
                "WHERE (l.user_id = ? OR f.user_id = ?)";
        try {
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ownerId);
            pstmt.setInt(2, ownerId);
            return pstmt.executeQuery(); // caller must iterate and close
        } catch (SQLException e) {
            System.err.println("Fetch reports failed: " + e.getMessage());
            return null;
        }
    }

    // Update report status (e.g., Approved, Rejected)
    public static boolean updateReportStatus(int reportId, String newStatus) {
        String sql = "UPDATE reports SET status=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, reportId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update report status failed: " + e.getMessage());
            return false;
        }
    }

    // Get all reports submitted by a reporter
    public static ResultSet getReportsByReporter(int reporterId) {
        String sql = "SELECT r.id, r.lost_item_id, r.found_item_id, r.reporter_id, r.status, " +
                "l.item_name AS lost_name, l.location AS lost_location, l.date_lost AS lost_date, " +
                "f.item_name AS found_name, f.location AS found_location, f.date_found AS found_date " +
                "FROM reports r " +
                "LEFT JOIN lost_items l ON r.lost_item_id = l.id " +
                "LEFT JOIN found_items f ON r.found_item_id = f.id " +
                "WHERE r.reporter_id = ?";
        try {
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reporterId);
            return pstmt.executeQuery(); // caller must iterate and close
        } catch (SQLException e) {
            System.err.println("Fetch reporter reports failed: " + e.getMessage());
            return null;
        }
    }

    // Insert notification for reporter

    public static boolean insertNotification(int userId, int reportId, String message) {
        String sql = "INSERT INTO notifications(user_id, report_id, message) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, reportId);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Notification insert failed: " + e.getMessage());
            return false;
        }
    }

    //  Delete notification by reportId
    public static boolean deleteNotificationByReportId(int reportId) {
        String sql = "DELETE FROM notifications WHERE report_id=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            int affected = pstmt.executeUpdate();
            System.out.println("Delete attempt for report_id=" + reportId + ", rows affected=" + affected);
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Delete notification failed: " + e.getMessage());
            return false;
        }
    }
    public static boolean deleteReport(int reportId) {
        String sql = "DELETE FROM reports WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Delete report failed: " + e.getMessage());
            return false;
        }
    }
    public static ResultSet getNotificationsForUser(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id=? ORDER BY timestamp DESC";
        try {
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Fetch notifications failed: " + e.getMessage());
            return null;
        }
    }
    public static int getOwnerIdForLostItem(int lostItemId) {
        String sql = "SELECT user_id FROM lost_items WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, lostItemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("user_id");
        } catch (SQLException e) {
            System.err.println("Owner fetch failed: " + e.getMessage());
        }
        return -1;
    }

    public static int getOwnerIdForFoundItem(int foundItemId) {
        String sql = "SELECT user_id FROM found_items WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, foundItemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("user_id");
        } catch (SQLException e) {
            System.err.println("Owner fetch failed: " + e.getMessage());
        }
        return -1;
    }
}