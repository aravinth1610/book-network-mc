package com.book.network.DTO;

import java.util.Set;

import com.book.network.modal.AuthRoutes;
import com.book.network.modal.Roles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRoutesRequest {
	
	private String path;
	
	private String redirectTo;
	
	private String pathMatch;
	
	private String component;
	
	private String canActivate;
	
	private String canDeactivate;

	private String apiEndPoint;
	
	private String permission;

	private Set<AuthRoutes> childrenAuthRoute;

//	private AuthRoutes parentAuthRoute;
	
	private Set<Roles> roles;


}
