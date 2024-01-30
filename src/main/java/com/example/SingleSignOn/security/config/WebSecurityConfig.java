package com.example.SingleSignOn.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.example.SingleSignOn.models.Permission.ADMIN_READ;
import static com.example.SingleSignOn.models.Permission.USER_READ;
import static com.example.SingleSignOn.models.Role.ADMIN;
import static com.example.SingleSignOn.models.Role.USER;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;

    private static final String[] WHITE_LIST_URL = {"/api/users/**",
            "/register",
            "/logout",
            "/login",
            "/",
            "/h2-console/**",
    };

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info(ADMIN.name());
        log.info(ADMIN_READ.name());
        log.info(USER.name());
        log.info(USER_READ.name());
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/dashboard/**").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/dashboard/**").hasAnyAuthority(USER_READ.name())
//                                .requestMatchers(HttpMethod.GET, "/dashboard/**").hasAnyAuthority(USER_READ.name())
//                                .requestMatchers(HttpMethod.PUT, "/dashboard/**").hasAnyAuthority(USER_READ.name())
//                                .requestMatchers(HttpMethod.PATCH, "/dashboard/**").hasAnyAuthority(USER_READ.name())
//                                .requestMatchers(HttpMethod.DELETE, "/dashboard/**").hasAnyAuthority(USER_READ.name())
                                .anyRequest()
                                .permitAll()
                )
                .authenticationProvider(authenticationProvider).headers((headers) -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                )).logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
//                .logout(logout -> logout.logoutUrl("/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                )
        ;

        return http.build();
    }
}