package com.example.SingleSignOn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Assuming you have a register.html in src/main/resources/templates
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "logout"; // Assuming you have a logout.html in src/main/resources/templates
    }
}


