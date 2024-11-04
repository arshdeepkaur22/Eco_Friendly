package com.example.controllers;

import org.springframework.web.bind.annotation.*;

import com.example.datahandlers.CartHandler;
import com.example.datahandlers.ProductHandler;
import com.example.datahandlers.UserHandler;

import java.util.Map;
import java.util.List;
import java.util.Collections;

@RestController
@CrossOrigin(origins = "*") // Allow CORS for all routes
@RequestMapping("/api")
public class Controller {

    UserHandler userHandler;
    ProductHandler productHandler;
    CartHandler cartHandler;

    Controller() {
        try {
            userHandler = new UserHandler("database.db");
            productHandler = new ProductHandler("database.db");
            cartHandler = new CartHandler("database.db");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // GET route
    @GetMapping("/greet")
    public String greet() {
        return "Hello, Welcome to Spring Boot!";
    }

    // Allowed types
    @GetMapping("/profile/{type}/{value}")
    public Map<String, Object> getProfile(@PathVariable String type, @PathVariable String value) {
        try {
            return userHandler.get_user(type, value);
        } catch (Exception e) {
            System.out.println(e.toString());
            return Collections.emptyMap();
        }
    }

    // get all products
    @GetMapping("/allProducts")
    public List<Map<String, Object>> allProducts() {
        try {
            return productHandler.getAllProducts();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // get cart items via id
    @GetMapping("/cart/{id}")
    public List<Map<String, Object>> getCart(@PathVariable String id) {
        try {
            return cartHandler.listProducts(id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // POST route for form data
    @PostMapping("/submitForm")
    public String submitForm(@RequestBody Map<String, String> formData) {
        String name = formData.get("name");
        String email = formData.get("email");
        return "Form submitted: Name = " + name + ", Email = " + email;
    }

    // PUT route for updating data
    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable int id, @RequestBody Map<String, String> updatedData) {
        return "User with ID " + id + " updated: " + updatedData;
    }

    // DELETE route for deleting user
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        return "User with ID " + id + " deleted!";
    }
}
