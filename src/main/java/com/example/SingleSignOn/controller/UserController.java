package com.example.SingleSignOn.controller;

import com.example.SingleSignOn.models.requests.RegisterRequest;
import com.example.SingleSignOn.models.Role;
import com.example.SingleSignOn.models.User;
import com.example.SingleSignOn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        return userService.findByEmail(email).get();
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterRequest user) throws IllegalAccessException {
        // You should validate input, handle errors, etc.
        return userService.registerUser(user);
    }

    @PutMapping("/{targetUserName}")
    public ResponseEntity<HttpStatus> updateUser(
            Principal principal,
            @PathVariable String targetEmail,
            @RequestBody RegisterRequest updateUserRequest) {

        try {
            User updatedUser = userService.updateUser(principal, targetEmail, updateUserRequest);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }


    @GetMapping("/by-role/{role}")
    public List<User> getUsersByRole(@PathVariable Role role) {
        return userService.findAllByRole(role);
    }
}
