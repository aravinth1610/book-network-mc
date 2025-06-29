package com.book.network.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.book.network.DTO.User;
import com.book.network.keycloakConfig.KeycloakSecurityUtil;

import jakarta.ws.rs.core.Response;

@Service
public class UserKcServices {

	private final ClientKcServices clientKcservices;

	private final KeycloakSecurityUtil keycloakUtil;
	
	public UserKcServices(KeycloakSecurityUtil keycloakUtil,ClientKcServices clientKcservices) {
		this.keycloakUtil = keycloakUtil;
		this.clientKcservices = clientKcservices;
	}

	public UserRepresentation createUser(Long orgId ,User user) {
		
		//Init the Keycloak
		keycloakUtil.initKeycloak(orgId);
		
		UserRepresentation userRep = mapUserRep(user);
		Response response = getUsersResource().create(userRep);
		System.out.println(response.getStatus() + "--" + response.getEntity().toString());
		if (Objects.equals(201, response.getStatus())) {
			return userRep;
		} else {
			throw new RuntimeException("Retry it...");
		}
	}

	public UserRepresentation getUserById(String userId) {
		return getUsersResource().get(userId).toRepresentation();
	}

	public List<UserRepresentation> getListofUser() {
		return getUsersResource().list();
	}

	public void updateUser(String userId, User user) {
		UserRepresentation userRepresentation = getUsersResource().get(userId).toRepresentation();
		if (userRepresentation != null) {
			String userIdD = userRepresentation.getId();
			user.setId(userIdD);
			userRepresentation = UpdatemapUserRep(user);
			getUsersResource().get(userIdD).update(userRepresentation);
		} else {
			throw new RuntimeException("User not found!");
		}

	}

	public void deleteUser(String userId) {
		getUsersResource().delete(userId);
	}

	public RoleRepresentation assignUserRoles(String id, String roleName) {
		UsersResource usersResource = getUsersResource();
		RolesResource rolesResource = getRolesResource();

		// Step 1: Validate if the user exists
		UserRepresentation user = usersResource.get(id).toRepresentation();
		if (user == null) {
			throw new RuntimeException("User ID not found in Keycloak: " + id);
		}

		// Step 2: Validate if the role exists
		RoleRepresentation roleRep;
		try {
			roleRep = rolesResource.get(roleName).toRepresentation();
		} catch (Exception e) {
			throw new RuntimeException("Role not found in Keycloak: " + roleName);
		}

		// List<RoleRepresentation> roles = getRolesResource().list();
		// .forEach(role -> System.out.println("Available Role: " + role.getName()));

		//  Step 3: Assign role
		usersResource.get(id).roles().realmLevel().add(Arrays.asList(roleRep));

		return roleRep;
	}

	public List<RoleRepresentation> getListOfUserRoles(String id, String clientId) {
		String clientUUID = clientKcservices.getClientId(clientId).getId();
		List<RoleRepresentation> clientRoles = new ArrayList<RoleRepresentation>();
		clientRoles.addAll(getUsersResource().get(id).roles().realmLevel().listAll());
		clientRoles.addAll(getUsersResource().get(id).roles().clientLevel(clientUUID).listAll());
		return clientRoles;
	}

	private UsersResource getUsersResource() {
		return getRealmResource().users();
	}

	private RolesResource getRolesResource() {
		return getRealmResource().roles();
	}

	private RealmResource getRealmResource() {
		return keycloakUtil.getRealmResource();
	}

	private UserRepresentation mapUserRep(User user) {
		UserRepresentation userRep = new UserRepresentation();
		userRep.setId(user.getId());
		userRep.setUsername(user.getUserName());
		userRep.setFirstName(user.getFirstName());
		userRep.setLastName(user.getLastName());
		userRep.setEmail(user.getEmail());
		userRep.setEnabled(true);
		userRep.setEmailVerified(true);
		List<CredentialRepresentation> creds = new ArrayList<>();
		CredentialRepresentation cred = new CredentialRepresentation();
		cred.setTemporary(false);
		cred.setValue(user.getPassword());
		creds.add(cred);
		userRep.setCredentials(creds);
		return userRep;
	}

	private UserRepresentation UpdatemapUserRep(User user) {
		UserRepresentation userRep = new UserRepresentation();
		userRep.setId(user.getId());
		userRep.setFirstName(user.getFirstName());
		userRep.setLastName(user.getLastName());
		userRep.setEmail(user.getEmail());
		userRep.setEnabled(true);
		userRep.setEmailVerified(true);
		List<CredentialRepresentation> creds = new ArrayList<>();
		CredentialRepresentation cred = new CredentialRepresentation();
		cred.setTemporary(false);
		cred.setValue(user.getPassword());
		creds.add(cred);
		userRep.setCredentials(creds);
		return userRep;
	}

}
