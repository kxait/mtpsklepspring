package com.mytoporazka.mtsklepspring.controllers;

import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import com.mytoporazka.mtsklepspring.model.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Controller()
public class LoginLogoutController {
    private final LoginSessionManager loginSessionManager;

    public LoginLogoutController(LoginSessionManager loginSessionManager) {
        this.loginSessionManager = loginSessionManager;
    }

    @GetMapping("/login")
    public String login(
            @CookieValue(value = "id", defaultValue = "") String id,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        var cookies = request.getCookies();
        var idCookie = cookies == null
            ? Optional.empty()
            : Arrays.stream(cookies).filter(c ->
                c.getName().equals("id") &&
                c.getValue() != null &&
                !c.getValue().equals(""))
                .findFirst();

        if(idCookie.isPresent()) {
            var hasSession = loginSessionManager.existsSession(((Cookie)idCookie.get()).getValue());
            if(hasSession) {
                return "redirect:/app/menu";
            }
        }

        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String postForm(@ModelAttribute LoginForm loginForm, Model model, HttpServletResponse response) {
        model.addAttribute("loginForm", loginForm);

        var sessionId = loginSessionManager.getNewSession(
                loginForm.getEmail(),
                loginForm.getPassword());

        if(sessionId.isPresent()) {
            var cookie = new Cookie("id", sessionId.get());
            response.addCookie(cookie);
            return "redirect:/app/menu";
        }

        model.addAttribute("error", "Niepoprawny email lub haslo");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        var cookie = new Cookie("id", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
