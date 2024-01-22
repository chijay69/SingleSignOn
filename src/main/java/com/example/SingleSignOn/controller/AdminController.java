package com.example.SingleSignOn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard"; // Create the corresponding admin/dashboard.jsp or admin/dashboard.html
    }

    @GetMapping("/admin/manage-users")
    public String manageUsers() {
        return "admin/manageUsers"; // Create the corresponding admin/manageUsers.html or admin/manageUsers.html
    }

    // Add more admin-specific mappings as needed
}
