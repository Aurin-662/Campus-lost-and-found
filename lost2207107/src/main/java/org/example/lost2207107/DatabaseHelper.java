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

            // Reports table
            String sqlReports = "CREATE TABLE IF NOT EXISTS reports (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "lost_item_id INTEGER," +
                    "found_item_id INTEGER," +
                    "status TEXT DEFAULT 'Pending'," +
                    "FOREIGN KEY(lost_item_id) REFERENCES lost_items(id)," +
                    "FOREIGN KEY(found_item_id) REFERENCES found_items(id))";
            stmt.execute(sqlReports);

            System.out.println("✅ Database initialized with expanded tables.");

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

    // Create report
    public static boolean createReport(int lostItemId, int foundItemId) {
        String sql = "INSERT INTO reports(lost_item_id, found_item_id, status) VALUES(?, ?, 'Pending')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, lostItemId);
            pstmt.setInt(2, foundItemId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Report creation failed: " + e.getMessage());
            return false;
        }
    }
}