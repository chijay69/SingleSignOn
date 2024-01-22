package com.example.SingleSignOn.security.config;

import com.example.SingleSignOn.controller.AdminController;
import com.example.SingleSignOn.security.PasswordEncoder;
import com.example.SingleSignOn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.example.SingleSignOn.models.Role.ADMIN;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
                                .requestMatchers("GET", "/admin/**").hasAnyRole(String.valueOf(ADMIN))
                        //TODO
                );

        return http.build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/api/**")
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/",).hasAnyRole("ADMIN","USER","ANONYMOUS")
//                        .requestMatchers("/admin/**").hasAnyRole("ADMIN","USER","ANONYMOUS")
//                        .requestMatchers("/api/user/").hasRole("USER")
//                        .anyRequest().authenticated()
//                );
//        return http.build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/", "/register.html", "/admin/**", "/static/**").permitAll()
//                        .requestMatchers("/api/user/**").hasRole("USER")
//                        .anyRequest().permitAll()
//                );
//        return http.build();
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .requestMatchers("/", "/register", "/public/**", "/static/**").permitAll()
//                .requestMatchers("/admin","/admin/**").hasRole("ADMIN")
//                .requestMatchers("user/","/user/**").hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated().and()
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .and().logout().permitAll();
//    }

//
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder()); // Use a new instance here
//        provider.setUserDetailsService(userService);
//        return provider;
//    }
}