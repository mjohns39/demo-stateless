package com.demo.authserver;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.authserver.user.model.Privilege;
import com.demo.authserver.user.model.User;
import com.demo.authserver.user.repo.PrivilegeRepository;
import com.demo.authserver.user.repo.UserRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoAuthServerApplication {

	@Autowired
	PasswordEncoder encoder;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoAuthServerApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner initUsers(UserRepository userRepository, PrivilegeRepository privilegeRepository) {
		return (args) -> {
			Privilege p1 = new Privilege();
			p1.setName("ROLE_USER");
			privilegeRepository.save(p1);
			
			
			
			User u1 = new User();
			User u2 = new User();
			
			u1.setUsername("user");
			u1.setPassword(encoder.encode("pass"));
			u1.setPrivileges(new HashSet<Privilege>(Arrays.asList(privilegeRepository.findByName("ROLE_USER"))));
			
//			u2.setUsername("admin");
//			u2.setPassword("adminpass");
//			u2.setRoles(Arrays.asList("ROLE_ADMIN"));
			
			userRepository.save(u1);
//			userRepository.save(u2);
		};
	}
}
