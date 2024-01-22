package com.example.SingleSignOn.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('admin:read')")
    public String adminDashboard() {
        return "admin/dashboard"; // Create the corresponding admin/dashboard.jsp or admin/dashboard.html
    }

    @GetMapping("/manage-users")
    @PreAuthorize("hasAuthority('admin:delete')")
//    @Hidden
    public String manageUsers() {
        return "admin/manageUsers"; // Create the corresponding admin/manageUsers.html or admin/manageUsers.html
    }

    // Add more admin-specific mappings as needed
}
