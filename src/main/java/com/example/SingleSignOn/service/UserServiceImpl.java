package com.example.SingleSignOn.service;

import com.example.SingleSignOn.converter.UserConverter;
import com.example.SingleSignOn.models.requests.ChangePasswordRequest;
import com.example.SingleSignOn.models.requests.RegisterRequest;
import com.example.SingleSignOn.models.Role;
import com.example.SingleSignOn.models.User;
import com.example.SingleSignOn.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
    public User registerUser(RegisterRequest userRequest) throws IllegalAccessException {
        boolean userExist = userRepository.findById(userRequest.getUsername()).isPresent();
        if (userExist) {
            throw new IllegalAccessException(String.format(EMAIL_EXCEPTION_MSG));
        }
        var user = User.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .username(userRequest.getUsername())
                // You should encode the password before saving it to the database
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public User updateUser(Principal principal, String targetUserName, RegisterRequest updateUserRequest) {
        String requestingUserName = principal.getName();

        // Check if the requesting user has the right to update the target user
        boolean isAdmin = isAdmin(requestingUserName);

        if (!isAdmin && !requestingUserName.equals(targetUserName)) {
            throw new AccessDeniedException("You do not have permission to update details for this user");
        }

        // Proceed with updating user information
        Optional<User> existingUser = userRepository.findById(targetUserName);
        User user = existingUser.orElseThrow(() -> new UsernameNotFoundException("No User found with username: " + targetUserName));

        // Proceed with updating user information
        user.setLastname(updateUserRequest.getLastname());
        user.setFirstname(updateUserRequest.getFirstname());
        user.setEmail(updateUserRequest.getEmail());
        user.setRole(updateUserRequest.getRole());

        return userRepository.save(user);
    }
    private boolean isAdmin(String requestingUserName) {
        return userRepository.findById(requestingUserName)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_MSG));
    }

    @Override
    public void changeUserPassword(Principal connectedUser, ChangePasswordRequest newPasswordRequest) {
        String requestingUserName = connectedUser.getName();
        Optional<User> optionalRequestingUser = userRepository.findById(requestingUserName);
        User requestingUser = UserConverter.convertOptionalToUser(optionalRequestingUser);

        if (passwordEncoder.matches(newPasswordRequest.getCurrentPassword(), requestingUser.getPassword())){
            throw new IllegalStateException("Wrong Password");
        }
        if (!newPasswordRequest.getNewPassword().equals(newPasswordRequest.getConfirmPassword())){
            throw new IllegalStateException("Passwords Are Not The Same");
        }
        String encodedPassword = passwordEncoder.encode(newPasswordRequest.getNewPassword());
        requestingUser.setPassword(encodedPassword);
        userRepository.save(requestingUser);
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
