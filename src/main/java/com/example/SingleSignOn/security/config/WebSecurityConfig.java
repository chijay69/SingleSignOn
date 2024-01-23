package com.example.SingleSignOn.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.example.SingleSignOn.models.Role.ADMIN;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;

    private static final String[] WHITE_LIST_URL = {"/api/users/**",
            "/register",
            "/logout",
    };

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers("GET", "/admin/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("POST", "/admin/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("PUT", "/admin/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("PATCH", "/admin/**").hasAnyRole(ADMIN.name())
                                .anyRequest()
                                .authenticated()
                )
                .authenticationProvider(authenticationProvider)
//                .logout(logout -> logout.logoutUrl("/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                )
        ;

        return http.build();
    }
}