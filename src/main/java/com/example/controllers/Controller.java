package com.example.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // Allow CORS for all routes
@RequestMapping("/api")
public class Controller {

    // GET route
    @GetMapping("/greet")
    public String greet() {
        return "Hello, Welcome to Spring Boot!";
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
