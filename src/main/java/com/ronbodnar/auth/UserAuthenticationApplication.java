package com.ronbodnar.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ronbodnar.auth.user.User;
import com.ronbodnar.auth.user.UserRepository;

@SpringBootApplication
public class UserAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthenticationApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			User user = new User("test@mail.com", passwordEncoder.encode("test"));
			userRepository.save(user);
			System.out.println("Added user: " + user.getEmail());
		};
	}
}
