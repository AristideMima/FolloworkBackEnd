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
//			userService.saveRole(new Role(null, "ROLE_USER"));
//			userService.saveRole(new Role(null, "ROLE_ADMIN"));
//			userService.saveRole(new Role(null, "ROLE_MANAGER"));
//			userService.saveRole(new Role(null, "ROLE_ANALYST"));
//
//			userService.saveUser(new User(null, "MISSITANE", "ARISTIDE MISSITANE", "mathieu_missitane@afrilandfirstbank.com", "mathieu_missitane", "mathieu_missitane", new HashSet<>()));
//			userService.saveUser(new User(null, "PONE", "GILLES", "gilles_pone@afrilandfirstbank.com", "gilles_pone", "gilles_pone", new HashSet<>()));
//			userService.saveUser(new User(null, "NDE OUFO", "ARCHIMEDE", "archimede_ndearchimede_nde@afrilandfirstbank.com", "archimede_nde", "archimede_nde", new HashSet<>()));
//			userService.saveUser(new User(null, "DOPGANG", "Wilfried", "wilfried_dopgang@gmail.com", "wilfried_dopgang", "wilfried_dopgang", new HashSet<>()));
//
//
//			userService.addRoleToUser("mathieu_missitane", "ROLE_ANALYST");
//			userService.addRoleToUser("mathieu_missitane", "ROLE_MANAGER");
//			userService.addRoleToUser("archimede_nde", "ROLE_MANAGER");
//			userService.addRoleToUser("gilles_pone", "ROLE_ANALYST");
//			userService.addRoleToUser("wilfried_dopgang", "ROLE_ANALYST");
//
//		};
//	}
}
 
