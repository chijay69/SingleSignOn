package com.example.SingleSignOn;

import com.example.SingleSignOn.models.requests.RegisterRequest;
import com.example.SingleSignOn.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.SingleSignOn.models.Role.ADMIN;
import static com.example.SingleSignOn.models.Role.USER;

@SpringBootApplication
public class SingleSignOnApplication {

	public static void main(String[] args) {
		SpringApplication.run(SingleSignOnApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			UserService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.username("admin")
					.email("admin@mail.com")
					.password("password")
					.locked(false)
					.enabled(true)
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.registerUser(admin));
			var user = RegisterRequest.builder()
					.firstname("User")
					.lastname("User")
					.username("user")
					.email("user@mail.com")
					.password("password")
					.locked(false)
					.enabled(true)
					.role(USER)
					.build();
			System.out.println("User token: " + service.registerUser(user));

		};
	}
}
