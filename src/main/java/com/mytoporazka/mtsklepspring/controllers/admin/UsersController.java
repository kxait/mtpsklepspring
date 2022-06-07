package com.mytoporazka.mtsklepspring.controllers.admin;

import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {
    private final DatabaseRepository repository;

    public UsersController (DatabaseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/app/admin/users")
    public String users(Model model) {
        var allUsers = repository.user.getAllByPredicate(u -> true);
        model.addAttribute("users", allUsers);

        return "app/admin/users";
    }
}
