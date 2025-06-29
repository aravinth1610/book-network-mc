package com.book.network.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.book.network.DTO.User;
import com.book.network.services.UserKcServices;
import com.unicore.customeResponse.ResponseEntityWrapper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class KcUserController {

	private final UserKcServices userKcServices;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/orgId/{orgId}/user")
	public ResponseEntityWrapper<?>  createUser(@PathVariable(value="orgId") Long orgId ,@RequestBody User user) {
		System.out.println(user);
		return new ResponseEntityWrapper<>(userKcServices.createUser(orgId, user));
	}

	@GetMapping("/user/{id}")
	public ResponseEntityWrapper<?> getUserById(@PathVariable(value = "id") String id) {
		System.out.println(id);
		return new ResponseEntityWrapper<>(userKcServices.getUserById(id));
	}

	@GetMapping("/user")
	public ResponseEntityWrapper<?> getUser() {
		return new ResponseEntityWrapper<>(userKcServices.getListofUser());
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/user/{id}")
	public ResponseEntityWrapper<?> deleteUser(@PathVariable(value = "id") String id) {
		userKcServices.deleteUser(id);
		return new ResponseEntityWrapper<>();
	}

	@PutMapping("/user/{id}")
	public ResponseEntityWrapper<?> updateUser(@PathVariable String id, @RequestBody User user) {
		System.out.println(id+"-"+user);
		userKcServices.updateUser(id, user);
		return new ResponseEntityWrapper<>();
	}
	
	@PostMapping(value = "/user/{id}/role")
	public ResponseEntityWrapper<?> assignUserRole(@PathVariable(name="id") String id, @RequestParam(name="role") String role) {
		System.out.println(id+"-"+role);
		return new ResponseEntityWrapper<>(userKcServices.assignUserRoles(id, role));
	}
	
	@GetMapping("/user/{id}/role")
	public ResponseEntityWrapper<?> getUser(@PathVariable("id") String id, @RequestParam("clientId") String clientId) {
		System.out.println(id + "-" + clientId);
		return new ResponseEntityWrapper<>(userKcServices.getListOfUserRoles(id, clientId));
	}

	
}
