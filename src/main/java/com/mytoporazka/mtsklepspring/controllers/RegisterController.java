package com.mytoporazka.mtsklepspring.controllers;

import com.mytoporazka.lib.domain.User;
import com.mytoporazka.lib.domain.UserStatus;
import com.mytoporazka.lib.domain.UserType;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import com.mytoporazka.mtsklepspring.model.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {
    private final DatabaseRepository repository;

    public RegisterController(DatabaseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "register";
    }

    @PostMapping("/register")
    public String postForm(@ModelAttribute LoginForm loginForm, Model model, HttpServletResponse response) {

        var userExists = repository.user.exists(u -> u.email.equals(loginForm.getEmail()));
        if(userExists > 0) {
            model.addAttribute("error", "Użytkownik już istnieje");
            return "register";
        }

        var userId = repository.user.getAllByPredicate(u -> true)
                .stream()
                .map(u -> u.userId)
                .reduce(0, Integer::max) + 1;

        var user = new User(userId, loginForm.getPassword(), loginForm.getEmail(), UserStatus.NEW, UserType.CUSTOMER);

        repository.user.add(user);
        repository.saveToDb();

        return "redirect:/login";
    }
}
