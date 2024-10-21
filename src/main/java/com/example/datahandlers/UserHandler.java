package com.example.datahandlers;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.utils.Tools;

public class UserHandler {
    private Connection connection;

    public UserHandler(String dbFileName) throws SQLException {
        // Initialize the SQLite database connection
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
        createUserTable();
    }

    private synchronized void createUserTable() throws SQLException {
        // Create the user table if it doesn't exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS user (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " + "email TEXT NOT NULL, " +
                "password TEXT NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            Tools.print("User table created");
        }
    }

    public synchronized void new_user(String name, String email, String password) throws SQLException {
        // Insert a new user into the database
        String insertSQL = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            Tools.print("User added " + name + " " + email + " " + password);
        }
    }

    public Map<String, String> get_user(String field, String value) throws SQLException {
        // Retrieve user data using user_id or name
        Map<String, String> userMap = new HashMap<>();
        String selectSQL = "SELECT * FROM user WHERE " + field + " = ? ";

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userMap.put("user_id", String.valueOf(rs.getInt("user_id")));
                userMap.put("name", rs.getString("name"));
                userMap.put("password", rs.getString("password"));
            }
        }
        return userMap;
    }

    public String getEmail(String id) throws SQLException {
        try {
            Map<String, String> value = get_user("user_id", id);
            return value.get("email");
        } catch (Exception e) {
            return "";
        }
    }

    public String getId(String email) throws SQLException {
        try {
            Map<String, String> value = get_user("email", email);
            return value.get("user_id");
        } catch (Exception e) {
            return "";
        }
    }

    public String getName(String id) throws SQLException {
        try {
            Map<String, String> value = get_user("user_id", id);
            return value.get("name");
        } catch (Exception e) {
            return "";
        }
    }

    public List<Map<String, String>> get_all_users() throws SQLException {
        // Retrieve all users and return as a list of maps
        List<Map<String, String>> usersList = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM user";

        try (PreparedStatement pstmt = connection.prepareStatement(selectAllSQL);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> userMap = new HashMap<>();
                userMap.put("user_id", String.valueOf(rs.getInt("user_id")));
                userMap.put("name", rs.getString("name"));
                userMap.put("email", rs.getString("email"));
                userMap.put("password", rs.getString("password"));
                usersList.add(userMap);
            }
        }
        return usersList;
    }

    public synchronized void delete_user(int userId) throws SQLException {
        // Delete a user by user_id
        String deleteSQL = "DELETE FROM user WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            Tools.print("User deleted: " + userId);
        }
    }

    public synchronized void update_user(int userId, String name, String password) throws SQLException {
        // Update user data while keeping previous values if not provided
        StringBuilder updateSQL = new StringBuilder("UPDATE user SET ");
        boolean first = true;

        if (name != null) {
            updateSQL.append("name = ? ");
            first = false;
        }

        if (password != null) {
            if (!first)
                updateSQL.append(", ");
            updateSQL.append("password = ? ");
        }

        updateSQL.append("WHERE user_id = ?");

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL.toString())) {
            int paramIndex = 1;

            if (name != null) {
                pstmt.setString(paramIndex++, name);
            }

            if (password != null) {
                pstmt.setString(paramIndex++, password);
            }

            pstmt.setInt(paramIndex, userId);
            pstmt.executeUpdate();
        }
    }

    public synchronized void close() throws SQLException {
        // Close the database connection
        if (connection != null) {
            connection.close();
        }
    }
}
