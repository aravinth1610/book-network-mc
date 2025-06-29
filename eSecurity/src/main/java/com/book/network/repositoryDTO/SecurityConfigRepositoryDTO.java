package com.book.network.repositoryDTO;

import java.util.Set;

import com.book.network.modal.Roles;

public interface SecurityConfigRepositoryDTO {

	Long getPkAuthRoute();
	
	String getApiEndPoint();
	
	String getPermission();
	
	String getCanActivate();
	
	Set<Roles> getRoles();
}
