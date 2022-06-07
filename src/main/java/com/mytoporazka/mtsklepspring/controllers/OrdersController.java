package com.mytoporazka.mtsklepspring.controllers;

import com.mytoporazka.lib.domain.*;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrdersController {
    private final DatabaseRepository repository;

    public OrdersController(DatabaseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/app/orders")
    public String orders(Model model, @RequestAttribute("userInfo") UserInfo userInfo) {
        model.addAttribute("userInfo", userInfo);

        // TODO: admin? else:
        var orders = userInfo.isAdmin
            ? repository.order.getAllByPredicate(p -> true)
            : repository.order.getAllByPredicate(p -> p.userID == userInfo.id);
        var orderBills = orders
                .stream()
                .map(o -> new OrderFullInfo(
                        repository.user.getFirstByPredicate(u -> u.userId == o.userID),
                        o,
                        repository.bill.getFirstByPredicate(b -> b.orderID == o.orderID)))
                .toList();

        model.addAttribute("orderBills", orderBills);

        return "app/orders";
    }

    @GetMapping("/app/orders/{id}")
    public String singleOrder(Model model, @RequestAttribute("userInfo") UserInfo userInfo, @PathVariable("id") int id) {
        model.addAttribute("userInfo", userInfo);
        var order = userInfo.isAdmin
                ? repository.order.getFirstByPredicate(o -> o.orderID == id)
                : repository.order.getFirstByPredicate(o -> o.orderID == id && o.userID == userInfo.id);
        if(order == null) {
            return "app/orders";
        }

        var bill = repository.bill.getFirstByPredicate(b -> b.orderID == order.orderID);
        var address = repository.address.getFirstByPredicate(a -> a.orderID == order.orderID);
        var entries = repository.orderEntry
                .getAllByPredicate(e -> e.orderID == order.orderID);

        var productQuantities = entries
                .stream()
                .map(e -> new OrderFullInfo.ProductQuantity(
                        repository.product.getFirstByPredicate(p -> p.productID == e.productID),
                        e.quantity))
                .toList();

        var user = repository.user.getFirstByPredicate(u -> u.userId == order.userID);

        var fullInfo = new OrderFullInfo(user, order, bill, productQuantities, address);

        model.addAttribute("orderInfo", fullInfo);

        return "app/orders";
    }

    static class OrderFullInfo {
        public User user;
        public Order order;
        public Bill bill;

        public List<ProductQuantity> products;

        public Address address;

        OrderFullInfo(User user, Order order, Bill bill) {
            this.user = user;
            this.order = order;
            this.bill = bill;
        }

        OrderFullInfo(User user, Order order, Bill bill, List<ProductQuantity> products, Address address) {
            this(user, order, bill);

            this.products = products;
            this.address = address;
        }

        static class ProductQuantity {
            public Product product;
            public int quantity;

            ProductQuantity(Product product, int quantity) {
                this.product = product;
                this.quantity = quantity;
            }
        }
    }
}
