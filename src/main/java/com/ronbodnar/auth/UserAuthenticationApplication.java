package com.ronbodnar.auth;

import com.ronbodnar.auth.user.User;
import com.ronbodnar.auth.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthenticationApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			User user1 = new User(0, "test", "test");
			User user2 = new User(1, "ron", "password");

			userRepository.save(user1);
			userRepository.save(user2);

			System.out.println("Added users to repo:");
			userRepository.findAll().forEach(System.out::println);
		};
	}
}