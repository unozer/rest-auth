package it.gp.rest_auth;

import it.gp.rest_auth.model.RoleEntity;
import it.gp.rest_auth.model.UserEntity;
import it.gp.rest_auth.service.RoleService;
import it.gp.rest_auth.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class RestAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAuthApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {
			roleService.save(new RoleEntity(null, "ROLE_USER"));
			roleService.save(new RoleEntity(null, "ROLE_ADMIN"));

			userService.save(new UserEntity(null, "rossi", "1234", new ArrayList<>()));
			userService.save(new UserEntity(null, "bianchi", "1234", new ArrayList<>()));

			userService.addRoleToUser("rossi", "ROLE_USER");
			userService.addRoleToUser("bianchi", "ROLE_ADMIN");
			userService.addRoleToUser("bianchi", "ROLE_USER");
		};
	}
}
