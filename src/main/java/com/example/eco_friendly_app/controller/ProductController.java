package com.ecoapp.controller;

import com.ecoapp.model.Product;
import com.ecoapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
public ResponseEntity<?> createProduct(@RequestBody Product product) {
    try {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Failed to add product: " + e.getMessage());
    }
}

@PostMapping
public ResponseEntity<?> createProduct(@RequestBody Product product) {
    try {
        logger.info("Attempting to create product: {}", product);
        Product createdProduct = productService.createProduct(product);
        logger.info("Product created successfully: {}", createdProduct);
        return ResponseEntity.ok(createdProduct);
    } catch (Exception e) {
        logger.error("Error creating product: ", e);
        return ResponseEntity.badRequest().body("Failed to add product: " + e.getMessage());
    }
}
}
