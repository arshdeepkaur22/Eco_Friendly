package com.example.demo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.datahandlers.UserHandler;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		// Create an instance of UserHandler
		try {
			UserHandler userHandler = new UserHandler("database.db");

			// Example usage: Adding a new user
			userHandler.new_user("John Doe", "password123");

			// Example usage: Getting a user by name
			Map<String, String> user = userHandler.get_user("John Doe");
			System.out.println("User: " + user);

			// Example usage: Getting all users
			List<Map<String, String>> allUsers = userHandler.get_all_users();
			System.out.println("All Users: " + allUsers);

			// Close the UserHandler when done
			userHandler.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
