package com.mytoporazka.mtsklepspring.controllers;

import com.mytoporazka.lib.domain.Product;
import com.mytoporazka.mtsklepspring.components.Cart;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class CartController {

    private final DatabaseRepository repository;
    private final Cart cart;

    public CartController(Cart cart, DatabaseRepository repository) {
        this.cart = cart;
        this.repository = repository;
    }

    @GetMapping("/app/cart")
    public String cart(Model model, @RequestAttribute UserInfo userInfo) {
        var cartItems = cart.getCartForUserId(userInfo.id);

        var cartEntries = cartItems
                .stream()
                .map(c -> getCartEntryByProductId(c.left, c.right))
                .toList();

        model.addAttribute("cart", cartEntries);

        return "app/cart";
    }

    private Product getProductById(int productId) {
        return repository.product.getFirstByPredicate(p -> p.productID == productId);
    }

    private CartEntry getCartEntryByProductId(int productId, int quantity) {
        var product = getProductById(productId);
        return new CartEntry(product.name, product.price, product.productID, quantity);
    }

    static class CartEntry {
        public String productName;
        public double productPrice;
        public int productId;
        public int quantity;

        public CartEntry(String productName, double productPrice, int productId, int quantity) {
            this.productName = productName;
            this.productPrice = productPrice;
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
