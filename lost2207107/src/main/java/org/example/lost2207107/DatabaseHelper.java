package org.example.lost2207107;

import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:users.db";

    // Static block: create users table if not exists
    static {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL)";
            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Database initialization failed:");
            e.printStackTrace();
        }
    }

    // Insert new user (signup)
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

    // Validate user (login)
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // user exists

        } catch (SQLException e) {
            System.err.println("Login validation failed:");
            e.printStackTrace();
            return false;
        }
    }
}