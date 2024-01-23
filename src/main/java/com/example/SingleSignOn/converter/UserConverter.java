package com.example.SingleSignOn.converter;

import com.example.SingleSignOn.models.User;

import java.util.Optional;

public class UserConverter {

    private UserConverter() {
        // private constructor to prevent instantiation
    }

    public static User convertOptionalToUser(Optional<User> userOptional) {
        return userOptional.map(UserConverter::convertUser).orElse(null);
    }

    private static User convertUser(User user) {
        // Assuming User has a copy constructor, if not, you can manually create a new User instance
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setRole(user.getRole());
        newUser.setEnabled(user.getEnabled());
        newUser.setLocked(user.getLocked());

        // Copy other fields as needed

        return newUser;
    }
}
