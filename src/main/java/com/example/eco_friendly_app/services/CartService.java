package com.ecoapp.service;

import com.ecoapp.model.Cart;
import com.ecoapp.model.CartItem;
import com.ecoapp.model.Product;
import com.ecoapp.model.User;
import com.ecoapp.repository.CartRepository;
import com.ecoapp.repository.ProductRepository;
import com.ecoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addToCart(String userId, String productId, int quantity) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (userOptional.isPresent() && productOptional.isPresent()) {
            User user = userOptional.get();
            Product product = productOptional.get();

            Cart cart = cartRepository.findByUserId(userId);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
            }

            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            } else {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                cart.getItems().add(newItem);
            }

            return cartRepository.save(cart);
        }

        return null;
    }

    public Cart removeFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            return cartRepository.save(cart);
        }
        return null;
    }

    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }
}