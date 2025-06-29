package com.book.network.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.book.network.modal.AuthOrgConfig;
import com.book.network.modal.AuthRoutes;
import com.book.network.services.AuthMenuServices;
import com.unicore.customeResponse.ResponseEntityWrapper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/authmenu")
public class AuthMenuController {

	private final AuthMenuServices serv;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ResponseEntityWrapper<?> createAuthMenu(@RequestBody List<AuthRoutes> menu) {
		System.out.println("menu"+menu);
		return new ResponseEntityWrapper<>(serv.createAuthRoutes(menu));
	}

	@GetMapping
	public ResponseEntityWrapper<?> getAllAuthMenu() {
		return new ResponseEntityWrapper<>(serv.getListOfAuthRoutes());
	}
	

	@GetMapping("/web")
	public ResponseEntityWrapper<?> getSecurityWebConfig() {
		return new ResponseEntityWrapper<>(serv.getSecurityConfigPermission());
	}
	
	@PostMapping("/org")
	public ResponseEntityWrapper<?> authConfig(@RequestBody AuthOrgConfig auth) {
		return new ResponseEntityWrapper<>(serv.authOrgConfig(auth));
	}

}
