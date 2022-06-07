package com.mytoporazka.mtsklepspring.controllers;

import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class ProductsController {
    private final DatabaseRepository repository;

    public ProductsController(DatabaseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/app/products")
    public String products(Model model, @RequestAttribute("userInfo") UserInfo userInfo) {
        var products = repository.product.getAllByPredicate(p -> !p.deleted);
        model.addAttribute("products", products);
        model.addAttribute("userInfo", userInfo);

        return "app/products";
    }
}
