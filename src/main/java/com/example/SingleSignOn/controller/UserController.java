package com.example.SingleSignOn.controller;

import com.example.SingleSignOn.models.User;
import com.example.SingleSignOn.models.UserRole;
import com.example.SingleSignOn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) throws IllegalAccessException {
        // You should validate input, handle errors, etc.
        return userService.createUser(user);
    }

    @PutMapping("/{userName}")
    public User updateUser(@PathVariable String userName, @RequestBody User updateUser) {
        return userService.updateUser(userName, updateUser);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }


    @GetMapping("/by-role/{role}")
    public List<User> getUsersByRole(@PathVariable UserRole userRole) {
        return userService.getUsersByRole(userRole);
    }
}
