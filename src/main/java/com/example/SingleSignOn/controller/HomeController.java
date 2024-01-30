package com.example.SingleSignOn.controller;

import com.example.SingleSignOn.models.requests.AuthenticationRequest;
import com.example.SingleSignOn.models.requests.RegisterRequest;
import com.example.SingleSignOn.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String homePage() {
        // You can return the name of the view/template you want to display for the home page
        return "mainViews/index";  // Assuming you have an "index.html" in src/main/resources/templates
    }

    @GetMapping("/register")
    public ModelAndView registerPage() {
        return new ModelAndView("authViews/register", "registerRequest", new RegisterRequest());
    }

    @PostMapping("/register")
    public RedirectView registerUser(@ModelAttribute("registerRequest") RegisterRequest registerRequest, RedirectAttributes attributes) {
        try {
            // Call the registration service to register the user
            userService.registerUser(registerRequest);

            // Assuming registration is successful, redirect to login page with a success message
            attributes.addFlashAttribute("registrationSuccess", true);
            return new RedirectView("authViews/login");
        } catch (Exception e) {
            log.error("Error during registration", e);
            // Handle registration failure, you might want to show an error message
            attributes.addFlashAttribute("registrationError", e.getMessage());
            return new RedirectView("/register");
        }
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "mainViews/index"; // Assuming you have a logout.html in src/main/resources/templates
    }

    @GetMapping("/login")
    public String loginPage() {
        return "authViews/login"; // Assuming you have a login.html in src/main/resources/templates
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Authentication> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(userService.authenticateUser(request));
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, RedirectAttributes attributes) {
        AuthenticationRequest authrequest = new AuthenticationRequest();
        authrequest.setEmail(email);
        authrequest.setPassword(password);

        Authentication authentication = userService.authenticateUser(authrequest);

        // Authentication successful, redirect to dashboard or any other page
        log.info("User authorities: {}", authentication.getAuthorities());

        if (!authentication.isAuthenticated()) {
            // Redirect to login or handle unauthenticated access
            log.info("User is not authenticated as authentication is null");
            return "redirect:/login";
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            log.info("User is admin, redirecting to admin dashboard");
            return "admin/dashboard";
            } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            log.info("User is not admin, redirecting to user dashboard");
//            return "redirect:/dashboard";
            return "mainViews/index";
        } else {
            log.error("Unexpected user role");
            throw new IllegalStateException("Unexpected user role");
        }
        // Redirect to dashboard with authentication information
//        attributes.addFlashAttribute("authentication", authentication);
//        return "redirect:/dashboard";
    }

//    public String redirectToDashboard(@ModelAttribute("authentication") Authentication authentication) {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "mainViews/index";
    }
}
