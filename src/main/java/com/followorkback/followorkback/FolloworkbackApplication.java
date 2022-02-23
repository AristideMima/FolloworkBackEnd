package com.followorkback.followorkback;

import com.followorkback.followorkback.entity.Role;
import com.followorkback.followorkback.entity.User;
import com.followorkback.followorkback.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;

@SpringBootApplication
public class FolloworkbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(FolloworkbackApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner run(UserService userService){
//		return args -> {
////			userService.saveRole(new Role(null, "ROLE_USER"));
////			userService.saveRole(new Role(null, "ROLE_ADMIN"));
////			userService.saveRole(new Role(null, "ROLE_MANAGER"));
////			userService.saveRole(new Role(null, "ROLE_ANALYST"));
//
//			userService.saveUser(new User(null, "Mima", "Aristide", "aristide@gmail.com", "aristidemima", "aris237", new HashSet<>()));
//			userService.saveUser(new User(null, "Nana", "Aristide", "abena@gmail.com", "calotest", "aris237", new HashSet<>()));
//			userService.saveUser(new User(null, "Mbappe", "Aristide", "manga@gmail.com", "mbappenana", "aris237", new HashSet<>()));
//			userService.saveUser(new User(null, "Mbappe", "Aristide", "mapemba@gmail.com", "manager_one", "aris237", new HashSet<>()));
//
//
//			userService.addRoleToUser("aristidemima", "ROLE_ANALYST");
////			userService.addRoleToUser("calotest", "ROLE_USER");
////			userService.addRoleToUser("mbappenana", "ROLE_MANAGER");
////			userService.addRoleToUser("mbappenanaigoire", "ROLE_ANALYST");
//
//
//
//
//		};
//	}
}
