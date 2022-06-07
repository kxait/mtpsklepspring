package com.mytoporazka.mtsklepspring.controllers.rest;

import com.mytoporazka.mtsklepspring.components.Cart;
import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CartRestController {
    private final Cart cart;
    private final LoginSessionManager loginSession;

    public CartRestController(Cart cart, LoginSessionManager loginSession) {
        this.cart = cart;
        this.loginSession = loginSession;
    }

    @PostMapping("/api/cart/{id}")
    public boolean addToCart(@RequestHeader("id") String auth, @PathVariable("id") int id, @RequestBody int quantity) {
        var userId = getUserIdForSession(auth);
        if(userId.isEmpty()) return false;

        cart.addToCart(userId.get(), id, quantity);
        return true;
    }

    @GetMapping("/api/cart/quantity")
    public long getQuantity(@RequestHeader("id") String auth) {
        var userId = getUserIdForSession(auth);
        if(userId.isEmpty()) return -1;
        return cart.cartQuantity(userId.get());
    }

    @PostMapping("/api/cart/clear")
    public void clearCart(@RequestHeader("id") String auth) {
        var userId = getUserIdForSession(auth);
        if(userId.isEmpty()) return;

        cart.clearCart(userId.get());
    }

    @DeleteMapping("/api/cart/{id}")
    public void removeFromCart(@RequestHeader("id") String auth, @PathVariable("id") int id, @RequestBody int quantity) {
        var userId = getUserIdForSession(auth);
        if(userId.isEmpty()) return;

        cart.removeFromCart(userId.get(), id, quantity);
    }

    private Optional<Integer> getUserIdForSession(String session) {
        if(!loginSession.existsSession(session)) return Optional.empty();
        return loginSession.getUserIdBySessionId(session);
    }

    static class CartEntry {
        public int productId;
        public int quantity;

        public CartEntry(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
