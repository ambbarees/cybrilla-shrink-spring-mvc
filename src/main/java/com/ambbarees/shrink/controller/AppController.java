package com.ambbarees.shrink.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ambbarees.shrink.model.User;
import com.ambbarees.shrink.repository.UserRepository;

@Controller
@Validated
public class AppController {

	static String USER_SESSION = "USER_SESSION";

	@Autowired
	static BCryptPasswordEncoder bCryptPasswordEncoderStatic;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String showHome(Model model, HttpSession session) {

		if (getLoggedInUser(userRepository) != null) {
			return "redirect:/account";
		}
		
		return "home";
	}

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public String register(@RequestParam(name = "username", required = true, defaultValue = "") String username,
			@RequestParam(name = "password", required = true, defaultValue = "") String password, Model model,
			HttpSession session) {

		if (getLoggedInUser(userRepository) != null) {
			return "redirect:/account";
		}

		if (username.isEmpty() || password.isEmpty()) {
			model.addAttribute("message", "Error! username or password cannot be empty");
			return "home";
		}

		User user = new User(username, bCryptPasswordEncoder.encode(password));
		try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("message",
					"Looks like '" + username + "' was already chosen. Please select another username.");
			return "home";
		}
		model.addAttribute("message", "Your account is registered. Please login now!");

		return "home";
	}
	
	@RequestMapping(path = "/oauth/success", method = RequestMethod.GET)
	public String register(Model model) {
		OidcUser user = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User newUser;
		
		Optional<User> optional = userRepository.findByUsername(user.getEmail());
		if (optional.isEmpty()) {
			newUser = new User(user.getEmail(), bCryptPasswordEncoder.encode(""));
			try {
				newUser = userRepository.save(newUser);
			} catch (Exception e) {
				return "redirect:/error";
			}
		}
				
		return "redirect:/account";
	}
	

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login() {
		if (getLoggedInUser(userRepository) != null) {
			return "redirect:/account";
		}
		return "login";
	}

	public static User getLoggedInUser(UserRepository repo) {
		try {
			Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (p instanceof User) {
				return (User) p;
			} else if (p instanceof OidcUser) {
				String email = ((OidcUser) p).getEmail();
				Optional<User> u = repo.findByUsername(email);
				return u.get();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
