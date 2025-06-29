package com.book.network.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicore.customeResponse.ResponseEntityWrapper;

@RestController
public class TokenController {
	
	@PostMapping("/validate")
	public ResponseEntityWrapper<?> validate() {
		System.out.println("==============/Valiate");
		Map<String, String> userId = new HashMap<>();
		Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();

		if (authenticationUser != null && authenticationUser.getName() != null) {
			userId.put("X-user-X-Id", authenticationUser.getName().toString());
	    }
		return new ResponseEntityWrapper<>(userId);
	}

	
}