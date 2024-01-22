package com.example.SingleSignOn.service;

import com.example.SingleSignOn.converter.UserConverter;
import com.example.SingleSignOn.models.User;
import com.example.SingleSignOn.models.UserRole;
import com.example.SingleSignOn.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final static String USER_NOT_FOUND_MSG ="user with email %s not found";
    private final static String USER_EXCEPTION_MSG ="user not found.\nException occurred: %s";
    private final static String EMAIL_EXCEPTION_MSG ="email taken already";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles("ROLE_" + user.getRole())
                        .build())
                .orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            Optional<User> userOptional = userRepository.findById(email);
            return UserConverter.convertOptionalToUser(userOptional);
        } catch (Exception e) {
            throw new RuntimeException(String.format(USER_EXCEPTION_MSG,e));
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) throws IllegalAccessException {
        boolean userExist = userRepository.findById(user.getUsername()).isPresent();
        if (userExist) {
            throw new IllegalAccessException(String.format(EMAIL_EXCEPTION_MSG));
        }
        // You should encode the password before saving it to the database
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public User updateUser(String userName, User updateUser) {
        Optional<User> existingUser = userRepository.findById(userName);
        if (existingUser.isEmpty()){
            throw new UsernameNotFoundException("No User found with username: "+ userName);
        }

        User user = UserConverter.convertOptionalToUser(existingUser);
        user.setUserName(updateUser.getUsername());
        user.setEmail(updateUser.getEmail());
        user.setRole(updateUser.getRole());
        String encodedPassword = passwordEncoder.encode(updateUser.getPassword());
        user.setPassword(encodedPassword);
        user.setLastName(updateUser.getLastName());
        user.setFirstName(updateUser.getFirstName());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    @Override
    public int enableUser(String userName){
        return userRepository.enableUser(userName);
    }
}
