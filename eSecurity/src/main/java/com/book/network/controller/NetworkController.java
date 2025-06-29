package com.book.network.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicore.customeResponse.ResponseEntityWrapper;

@RestController
public class NetworkController {


	@GetMapping("/books")
	public ResponseEntityWrapper<?> getData(Authentication  authenticationUser) { //
		System.out.println("----"+authenticationUser.getName()+"--"+authenticationUser.getPrincipal().toString()+"--"+authenticationUser.getAuthorities().toString());
		return new ResponseEntityWrapper<>(Arrays.asList(Map.of("Name", "Ram", "Age", 12, "Books", Arrays.asList("Jemmy", "Makers"))));
	}
}
