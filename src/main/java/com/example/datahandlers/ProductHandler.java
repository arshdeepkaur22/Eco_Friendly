package com.example.datahandlers;

import java.sql.*;
import java.util.*;

public class ProductHandler {
    private Connection connection;

    public ProductHandler(String dbFilePath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        createProductTable();
    }

    // Create the products table if it does not exist
    private synchronized void createProductTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "price REAL, " +
                "description TEXT, " +
                "CarbonEmission REAL)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    // Method to add a new product and return its product_id
    public synchronized int addProduct(String name, double price, String description, double carbonEmission)
            throws SQLException {
        String sql = "INSERT INTO products (name, price, description, CarbonEmission) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        pstmt.setDouble(2, price);
        pstmt.setString(3, description);
        pstmt.setDouble(4, carbonEmission);
        pstmt.executeUpdate();

        // Retrieve generated product_id
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return -1; // return -1 if insertion failed
    }

    // Method to get product ID by name
    public int getId(String name) throws SQLException {
        String sql = "SELECT product_id FROM products WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("product_id");
        }
        return -1; // return -1 if product not found
    }

    // Method to get product name by ID
    public String getName(int id) throws SQLException {
        String sql = "SELECT name FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        }
        return null; // return null if product not found
    }

    // Method to get product price by ID
    public double getPrice(int id) throws SQLException {
        String sql = "SELECT price FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getDouble("price");
        }
        return 0.0; // return 0.0 if product not found
    }

    // Method to get Carbon Emission by ID
    public double getCarbonEmission(int id) throws SQLException {
        String sql = "SELECT CarbonEmission FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getDouble("CarbonEmission");
        }
        return 0.0; // return 0.0 if product not found
    }

    // Method to set price for a product
    public synchronized void setPrice(int id, double price) throws SQLException {
        String sql = "UPDATE products SET price = ? WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setDouble(1, price);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }

    // Method to set Carbon Emission for a product
    public synchronized void setCarbonEmission(int id, double emission) throws SQLException {
        String sql = "UPDATE products SET CarbonEmission = ? WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setDouble(1, emission);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }

    // Method to get product description by ID
    public String getDescription(int id) throws SQLException {
        String sql = "SELECT description FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("description");
        }
        return null; // return null if product not found
    }

    // Method to get all product details as a map
    public Map<String, Object> getProduct(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Map<String, Object> product = new HashMap<>();
            product.put("product_id", rs.getInt("product_id"));
            product.put("name", rs.getString("name"));
            product.put("price", rs.getDouble("price"));
            product.put("description", rs.getString("description"));
            product.put("CarbonEmission", rs.getDouble("CarbonEmission"));
            return product;
        }
        return null; // return null if product not found
    }

    // Method to get all products
    public List<Map<String, Object>> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM products";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Map<String, Object>> products = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> product = new HashMap<>();
            product.put("product_id", rs.getInt("product_id"));
            product.put("name", rs.getString("name"));
            product.put("price", rs.getDouble("price"));
            product.put("description", rs.getString("description"));
            product.put("CarbonEmission", rs.getDouble("CarbonEmission"));
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
