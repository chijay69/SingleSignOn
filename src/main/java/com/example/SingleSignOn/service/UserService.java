package com.example.SingleSignOn.service;

import com.example.SingleSignOn.models.User;
import com.example.SingleSignOn.models.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserService extends UserDetailsService {

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User createUser(User user) throws IllegalAccessException;

    List<User> getUsersByRole(UserRole userRole);

    User updateUser(String userName, User updateUser);

    void deleteUser(String email);

    int enableUser(String userName);

}
