package com.ambbarees.shrink.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ambbarees.shrink.model.UrlMap;
import com.ambbarees.shrink.model.User;
import com.ambbarees.shrink.repository.UrlMapRepository;
import com.ambbarees.shrink.repository.UserRepository;
import com.ambbarees.shrink.utils.utils;

@RestController
@Validated
public class ShrinkController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UrlMapRepository urlMapRepo;

	@GetMapping("/shrink")
	@Validated
	public ResponseEntity<?> shrink(@RequestParam(name = "url", required = true) @NotBlank String url,
			@RequestParam(name = "key", required = true) @NotBlank String key) throws Exception {

		if (url.isEmpty()) {
			return new ResponseEntity<>(Map.of("error", "'url' cannot be empty"), HttpStatus.BAD_REQUEST);
		}

		if (key.isEmpty()) {
			return new ResponseEntity<>(Map.of("error", "'key' cannot be empty"), HttpStatus.BAD_REQUEST);
		}

		String keyHash = utils.Hash(key);
		User user = userRepo.findByKeyHash(keyHash);
		if (user == null) {
			return new ResponseEntity<>(
					Map.of("error", "The key supplied is invalid. Please use a different key or generate a new one."),
					HttpStatus.BAD_REQUEST);
		}

		if (!utils.isValidURL(url)) {
			return new ResponseEntity<>(Map.of("error", "The url '" + url + "' is invalid"), HttpStatus.BAD_REQUEST);
		}

		UrlMap entity = urlMapRepo.findByUrl(url);
		if (entity == null) {
			entity = new UrlMap(utils.randomString(), url);
			urlMapRepo.saveWithRetry(entity);
		}

		return new ResponseEntity<>(Map.of("url", "http://localhost:8080/u/" + entity.getId()), HttpStatus.OK);
	}

	@GetMapping("/u/{id}")
	public void redirect(@PathVariable(name = "id") String id, HttpServletResponse response) throws IOException {		
		Optional<UrlMap> result = urlMapRepo.findById(id);
		if (result.isEmpty()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Redirect Param");
			return;
		}

		response.sendRedirect(result.get().getUrl());
	}

}
