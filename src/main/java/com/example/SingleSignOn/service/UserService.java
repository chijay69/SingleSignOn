package com.example.SingleSignOn.service;

import com.example.SingleSignOn.models.requests.ChangePasswordRequest;
import com.example.SingleSignOn.models.requests.RegisterRequest;
import com.example.SingleSignOn.models.Role;
import com.example.SingleSignOn.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User registerUser(RegisterRequest userRequest) throws IllegalAccessException;

    List<User> getUsersByRole(Role role);

    User updateUser(Principal principal, String targetUserName, RegisterRequest updateUserRequest);

    void changeUserPassword(Principal connectedUser, ChangePasswordRequest newPasswordRequest);

    void deleteUser(String email);

    int enableUser(String userName);

}
