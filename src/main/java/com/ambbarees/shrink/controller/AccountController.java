package com.ambbarees.shrink.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ambbarees.shrink.model.User;
import com.ambbarees.shrink.repository.UserRepository;
import com.ambbarees.shrink.utils.utils;

@Controller
public class AccountController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(path = "/account", method = RequestMethod.GET)
	public String account(Model model, HttpSession session) {
		User user = AppController.getLoggedInUser(userRepository);
		model.addAttribute("message", user.getUsername());
		return "account";
	}

	@RequestMapping(path = "/account/generate-new-key", method = RequestMethod.POST)
	public String generateNewKey(Model model, HttpSession session) {
		User user = AppController.getLoggedInUser(userRepository);

		String key = UUID.randomUUID().toString().replace("-", "");
		String keyHash = utils.Hash(key);
		user.setKeyHash(keyHash);
		userRepository.save(user);

		model.addAttribute("message", user.getUsername());
		model.addAttribute("key", key);
		return "account";
	}
}
