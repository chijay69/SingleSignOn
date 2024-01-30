package com.example.SingleSignOn.service;

import com.example.SingleSignOn.models.Role;
import com.example.SingleSignOn.models.User;
import com.example.SingleSignOn.models.requests.AuthenticationRequest;
import com.example.SingleSignOn.models.requests.RegisterRequest;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);

    void deleteUser(String email);

    User updateUser(Principal principal, String targetEmail, RegisterRequest updateUserRequest);

    boolean isAdmin(String userEmail);

    User registerUser(RegisterRequest userRequest);

    List<User> getAllUsers();

    Authentication authenticateUser(AuthenticationRequest authenticationRequest);
}