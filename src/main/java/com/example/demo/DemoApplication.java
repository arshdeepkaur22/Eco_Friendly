package com.example.demo;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Files;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.datahandlers.CartHandler;
import com.example.datahandlers.ProductHandler;
import com.example.datahandlers.UserHandler;
import com.example.utils.ExcelToListConverter;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.controllers" }) // Add this line
public class DemoApplication {

	public static void main(String[] args) {
		initialiseDb();
		System.out.println("http://localhost:8080/api/greet");
		SpringApplication.run(DemoApplication.class, args);
	}

	static int initialiseDb() {
		String database = "database.db";
		String assets = "assets/";
		Path path = Paths.get(database);

		if (Files.exists(path)) {
			System.out.println("File exists.");
			return 1;
		}
		try {

			UserHandler userHandler = new UserHandler(database);
			CartHandler cartHandler = new CartHandler(database);
			ProductHandler productHandler = new ProductHandler(database);

			ArrayList<String> xlSheets = new ArrayList<String>();
			xlSheets.add("cart.xlsx");
			xlSheets.add("products.xlsx");
			xlSheets.add("user.xlsx");

			List<HashMap<String, String>> data = ExcelToListConverter.convertExcelToList(assets + xlSheets.get(0));
			for (HashMap<String, String> item : data) {
				cartHandler.addProduct(item.get("user_id"), item.get("product_id"));
			}

			data = ExcelToListConverter.convertExcelToList(assets + xlSheets.get(1));
			for (HashMap<String, String> item : data) {
				productHandler.addProduct(item.get("name"), Double.parseDouble(item.get("price")),
						item.get("description"),
						Double.parseDouble(item.get("CarbonEmission")));
			}

			data = ExcelToListConverter.convertExcelToList(assets + xlSheets.get(2));
			for (HashMap<String, String> item : data) {
				userHandler.new_user(item.get("name"), item.get("email"), item.get("password"));
			}

		} catch (Exception e) {
			System.out.println("Failed to add content to db.");
		}
		return 0;
	}
}
