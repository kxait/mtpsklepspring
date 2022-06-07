package com.mytoporazka.mtsklepspring.controllers;

import com.mytoporazka.mtsklepspring.components.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class MenuController {

    @GetMapping("/app/menu")
    public String menu(Model model, @RequestAttribute UserInfo userInfo) {
        model.addAttribute("userInfo", userInfo);
        return "app/menu";
    }
}