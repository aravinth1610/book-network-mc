package com.book.network.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.book.network.modal.Roles;
import com.book.network.services.RealmRoleKcServices;
import com.unicore.customeResponse.ResponseEntityWrapper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class KcRoleController {

	private final RealmRoleKcServices serv;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/realm/role")
	public ResponseEntityWrapper<?> createRole(@RequestBody Roles role) {
		System.out.println(role);
		 serv.createRole(role);
		 return new ResponseEntityWrapper<>();
	}
	
	@GetMapping("/realm/roles")
	public ResponseEntityWrapper<?> getListOfRoles() {
		 return new ResponseEntityWrapper<>(serv.getListOfRole());
	}

	@GetMapping("/realm/role/{roleName}")
	public ResponseEntityWrapper<?> getListOfRoles(@PathVariable(value="roleName") String roleName) {
		System.out.println(roleName);
		return new ResponseEntityWrapper<>(serv.getRole(roleName));
	}
	
	@PutMapping("/realm/role/{roleName}")
	public ResponseEntityWrapper<?> updateRole(@PathVariable String roleName, @RequestBody Roles role) {
		serv.updateRole(roleName, role);
		return new ResponseEntityWrapper<>();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/realm/role/{roleName}")
	public ResponseEntityWrapper<?> deleteRole(@PathVariable String roleName) {
		serv.deleteUser(roleName);
		return new ResponseEntityWrapper<>();
	}
	
}
