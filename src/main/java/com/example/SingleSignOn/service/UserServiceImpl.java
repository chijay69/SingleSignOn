package com.example.SingleSignOn.service;

import com.example.SingleSignOn.converter.UserConverter;
import com.example.SingleSignOn.models.Role;
import com.example.SingleSignOn.models.User;
import com.example.SingleSignOn.models.requests.RegisterRequest;
import com.example.SingleSignOn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email not found")));
    }

    @Override
    public List<User> findAllByRole(Role role) {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equals(role))
                .collect(Collectors.toList());
        return users;
    }

    @Override
    public void deleteUser(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()){
            throw new IllegalStateException("User email is invalid");
        }
        User user = UserConverter.convertOptionalToUser(optionalUser);
        userRepository.deleteById(user.getId());
    }

    @Override
    public User updateUser(Principal principal, String targetEmail, RegisterRequest updateUserRequest) {
        String requestingUserEmail = principal.getName();
        try{
            Optional<User> optionalRequestingUser = userRepository.findByEmail(requestingUserEmail);

            if (optionalRequestingUser.isEmpty()) {
                throw new UsernameNotFoundException("Requesting user not found.");
            }

            User requestingUser = optionalRequestingUser.get();
            String userEmail = requestingUser.getEmail();

            boolean isAdmin = isAdmin(userEmail);

            if (!isAdmin && !userEmail.equals(targetEmail)) {
                throw new AccessDeniedException("You do not have permission to update details for this user");
            }

            Optional<User> optionalTargetUser = userRepository.findByEmail(targetEmail);
            if (optionalTargetUser.isEmpty()) {
                throw new UsernameNotFoundException("Target user not found.");
            }

            User targetUser = optionalTargetUser.get();
            targetUser.setFirstname(updateUserRequest.getFirstname());
            targetUser.setLastname(updateUserRequest.getLastname());
            targetUser.setUsername(updateUserRequest.getUsername());
            targetUser.setEmail(updateUserRequest.getEmail());
            targetUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
            targetUser.setRole(updateUserRequest.getRole());


            System.out.println(String.format("User: %s updated successfully", targetUser.getUsername()));
            return userRepository.save(targetUser);
        } catch (RuntimeException e){
            System.out.println(String.format("Error occurred: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public boolean isAdmin(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return user.getRole() == Role.ADMIN;
    }

    @Override
    public User registerUser(RegisterRequest userRequest) {
        String userEmail = userRequest.getEmail();

        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email is already taken: " + userEmail);
        }

        var user = User.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .email(userEmail)
                .role(userRequest.getRole())
                .username(userRequest.getUsername())
                // You should encode the password before saving it to the database
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}