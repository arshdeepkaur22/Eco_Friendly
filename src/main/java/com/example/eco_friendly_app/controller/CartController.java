package com.ecoapp.controller;

import com.ecoapp.model.Cart;
import com.ecoapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable String userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam String userId, @RequestParam String productId, @RequestParam int quantity) {
        Cart updatedCart = cartService.addToCart(userId, productId, quantity);
        return updatedCart != null ? ResponseEntity.ok(updatedCart) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestParam String userId, @RequestParam String productId) {
        Cart updatedCart = cartService.removeFromCart(userId, productId);
        return updatedCart != null ? ResponseEntity.ok(updatedCart) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}