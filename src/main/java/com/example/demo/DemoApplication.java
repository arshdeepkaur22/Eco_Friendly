package com.example.demo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.datahandlers.UserHandler;
import com.example.datahandlers.CartHandler;
import com.example.datahandlers.ProductHandler;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		String database = "database.db";

		// Create instances of handlers
		try {
			UserHandler userHandler = new UserHandler(database);
			CartHandler cartHandler = new CartHandler(database);
			ProductHandler productHandler = new ProductHandler(database);

			// Example usage: Adding a new user
			userHandler.new_user("John Doe", "hello@world.com", "password123");

			// Example usage: Adding new products
			productHandler.addProduct("Product 1", 19.99, "Description for Product 1", 5.0);
			productHandler.addProduct("Product 2", 29.99, "Description for Product 2", 3.0);

			// Example usage: Getting product ID by name
			int productId = productHandler.getId("Product 1");
			System.out.println("Product ID for 'Product 1': " + productId);

			// Example usage: Getting product details
			Map<String, Object> productDetails = productHandler.getProduct(productId);
			System.out.println("Product Details: " + productDetails);

			// Example usage: Getting all products
			List<Map<String, Object>> allProducts = productHandler.getAllProducts();
			System.out.println("All Products: " + allProducts);

			// Example usage: Adding a product to the cart
			cartHandler.addProduct("1", String.valueOf(productId));

			// Example usage: Removing a product from the cart
			// cartHandler.removeProduct(1);

			// Example usage: Getting a user by name
			Map<String, String> user = userHandler.get_user("name", "John Doe");
			System.out.println("User: " + user);

			// Example usage: Getting all users
			List<Map<String, String>> allUsers = userHandler.get_all_users();
			System.out.println("All Users: " + allUsers);

			// Close the handlers when done
			userHandler.close();
			productHandler.close();
			cartHandler.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
