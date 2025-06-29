package com.book.network.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
public class RoleServices {

	private final UserKcServices userKcServices;

	private final RealmRoleKcServices realmRoleKcServices;

	private final ClientRoleKcServices clientRoleKcServices;

	public RoleServices( UserKcServices userKcServices, RealmRoleKcServices realmRoleKcServices,
			ClientRoleKcServices clientRoleKcServices )
	{
		this.userKcServices = userKcServices;
		this.realmRoleKcServices = realmRoleKcServices;
		this.clientRoleKcServices = clientRoleKcServices;
	}

	public Set<String> getGrantedAuthority(String sub, String clientId) {
		List<RoleRepresentation> roles = new ArrayList<>();

		List<RoleRepresentation> userLevelroles = userKcServices.getListOfUserRoles(sub, clientId);
		List<RoleRepresentation> realmRoles = realmRoleKcServices.getListOfRole();
		roles.addAll(userLevelroles);
		roles.addAll(realmRoles);

		return roles.stream().map(RoleRepresentation::getName).collect(Collectors.toSet());
	}

	public Set<String> getOverAllRolesNameForClientId(String clientId) {
		return clientRoleKcServices.getOverAllRolesForClientId(clientId);
	}
	
	public List<RoleRepresentation> getListOfUserRoles(String id, String clientId){
		return userKcServices.getListOfUserRoles(id, clientId);
	}

}
