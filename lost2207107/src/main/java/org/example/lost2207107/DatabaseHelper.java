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
}