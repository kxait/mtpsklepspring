package com.mytoporazka.mtsklepspring.controllers;

import com.mytoporazka.lib.domain.*;
import com.mytoporazka.mtsklepspring.components.Cart;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.UserInfo;
import com.mytoporazka.mtsklepspring.components.interfaces.Config;
import com.mytoporazka.mtsklepspring.model.OrderForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class CreateOrderController {
    private final Cart cart;
    private final DatabaseRepository repository;

    private final Config config;

    public CreateOrderController(Cart cart, DatabaseRepository repository, Config config) {
        this.repository = repository;
        this.cart = cart;
        this.config = config;
    }

    @GetMapping("/app/createOrder")
    public String order(Model model, @RequestAttribute("userInfo")UserInfo userInfo) {
        model.addAttribute("orderForm", new OrderForm());

        var cartItems = cart.getCartForUserId(userInfo.id);
        var cartEntries = getCartEntries(cartItems);
        var subtotal = getSubtotal(cartEntries);

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("postagePrice", config.getPostagePrice());
        model.addAttribute("cart", cartEntries);

        return "app/createOrder";
    }

    private List<CartController.CartEntry> getCartEntries(List<Cart.Tuple<Integer, Integer>> items) {
        return items
                .stream()
                .map(c -> getCartEntryByProductId(c.left, c.right))
                .toList();
    }

    private double getSubtotal(List<CartController.CartEntry> entries) {
        return entries.stream()
                .map(c -> c.productPrice * c.quantity)
                .reduce(0.0, Double::sum);
    }

    @PostMapping("/app/createOrder")
    public String orderForm(@ModelAttribute OrderForm orderForm, Model model, @RequestAttribute("userInfo") UserInfo userInfo) {
        var now = Instant.now();
        var orderId = repository.order.getAllByPredicate(o -> true)
                .stream()
                .map(o -> o.orderID)
                .reduce(0, Integer::max) + 1;

        var userCart = cart.getCartForUserId(userInfo.id);
        var cartEntries = getCartEntries(userCart);
        var subtotal = getSubtotal(cartEntries);

        var total = subtotal + config.getPostagePrice();

        var order = new Order(userInfo.id, orderId, now, OrderStatus.NEW);
        var bill = new Bill(orderId, now, now.plus(30, ChronoUnit.DAYS), total);
        var address = new Address(
                orderId,
                orderForm.street,
                orderForm.streetNumber,
                orderForm.postalCode,
                orderForm.city,
                orderForm.country,
                orderForm.phone);
        var domainOrderEntries = cartEntries
                .stream()
                .map(c -> new OrderEntry(c.quantity, orderId, c.productId))
                .toList();

        repository.order.add(order);
        repository.bill.add(bill);
        repository.address.add(address);
        domainOrderEntries.forEach(repository.orderEntry::add);

        repository.saveToDb();

        cart.clearCart(userInfo.id);

        model.addAttribute("success", true);
        return "app/createOrder";
    }

    private Product getProductById(int productId) {
        return repository.product.getFirstByPredicate(p -> p.productID == productId);
    }

    private CartController.CartEntry getCartEntryByProductId(int productId, int quantity) {
        var product = getProductById(productId);
        return new CartController.CartEntry(product.name, product.price, product.productID, quantity);
    }
}
