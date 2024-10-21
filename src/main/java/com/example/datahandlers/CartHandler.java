
package com.example.datahandlers;

import java.sql.*;
import java.util.*;
import com.example.utils.Tools;

public class CartHandler {
    private Connection connection;

    public CartHandler(String dbFilePath) throws SQLException {
        // Add busy_timeout parameter to the connection URL
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath + "?busy_timeout=30000");
        createCartTable();
    }

    // Create the cart table
    private synchronized void createCartTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS cart (entry_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, product_id TEXT)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        Tools.print("Cart table created");
    }

    // Method to add a product to the cart
    public synchronized void addProduct(String user_id, String product_id) throws SQLException {
        String sql = "INSERT INTO cart (user_id, product_id) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user_id);
        pstmt.setString(2, product_id);
        pstmt.executeUpdate();
        Tools.print("Added product: ");
    }

    // Method to remove a product from the cart
    public synchronized void removeProduct(int entry_id) throws SQLException {
        String sql = "DELETE FROM cart WHERE entry_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, entry_id);
        pstmt.executeUpdate();
        Tools.print("Removed product: ");
    }

    // Method to get the total number of products in the cart for a user
    public int getTotal(String user_id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user_id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1); // returns the total count of products
        }
        return 0;
    }

    // Method to list all products in the cart for a user
    public List<Map<String, String>> listProducts(String user_id) throws SQLException {
        String sql = "SELECT * FROM cart WHERE user_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user_id);
        ResultSet rs = pstmt.executeQuery();
        List<Map<String, String>> products = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> product = new HashMap<>();
            product.put("entry_id", String.valueOf(rs.getInt("entry_id")));
            product.put("user_id", rs.getString("user_id"));
            product.put("product_id", rs.getString("product_id"));
            products.add(product);
        }
        return products;
    }

    // Close the connection
    public synchronized void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
