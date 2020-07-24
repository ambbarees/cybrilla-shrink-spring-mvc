package com.ambbarees.shrink;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ambbarees.shrink.model.User;
import com.ambbarees.shrink.repository.UserRepository;
import com.ambbarees.shrink.utils.utils;

@SpringBootApplication
public class Application {

	static Logger logger = Logger.getLogger("main");

	@Autowired
	UserRepository userRepo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	private void init() {
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();	
		utils.init();		
		List<User> users = Arrays.asList(new User("admin", b.encode("admin")), new User("manager", b.encode("manager")));
		try {
			userRepo.saveAll(users);
		} catch (DataIntegrityViolationException e) {
			// ignore as the users are already present
		}
		
	}

}
