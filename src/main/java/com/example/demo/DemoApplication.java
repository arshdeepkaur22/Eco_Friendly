package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.controllers" }) // Add this line
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("http://localhost:8080/api/greet");
		SpringApplication.run(DemoApplication.class, args);
	}
}
