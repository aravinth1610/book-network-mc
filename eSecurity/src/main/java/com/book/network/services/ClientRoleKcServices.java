package com.book.network.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import com.book.network.DTO.AttributeDTO;
import com.book.network.DTO.ClientRoles;
import com.book.network.keycloakConfig.KeycloakSecurityUtil;
import com.book.network.modal.Roles;

@Service
public class ClientRoleKcServices {
	
	private final KeycloakSecurityUtil keycloakUtil;

	private final RealmRoleKcServices realmRoleKcServices;
	
	public ClientRoleKcServices(KeycloakSecurityUtil keycloakUtil, RealmRoleKcServices realmRoleKcServices) {
		this.keycloakUtil = keycloakUtil;
		this.realmRoleKcServices = realmRoleKcServices;
	}

	public void createClientRole(Set<ClientRoles> clientRoles) {
		RealmResource realmResource = getRealmResource();

		// Check if any client has "-001" to determine if additional roles are needed
		boolean hasFBEnd = clientRoles.stream().anyMatch(clientRole -> clientRole.getClientId().endsWith("-001"));

		// If there's no "-001", proceed directly
		Set<ClientRoles> newClientRoles = new HashSet<>(clientRoles);

		// If "-001" exists, add "-002" equivalent roles
		if (hasFBEnd) {
			clientRoles.stream().filter(clientRole -> clientRole.getClientId().endsWith("-001"))
					.map(clientRole -> new ClientRoles(clientRole.getClientId().replace("-001", "-002"),
							new HashSet<>(clientRole.getRoles())))
					.forEach(newClientRoles::add);
		}

		// Process roles for all clients
		for (ClientRoles clientRole : newClientRoles) {
			ClientRepresentation client = realmResource.clients().findByClientId(clientRole.getClientId()).stream()
					.findFirst().orElseThrow(() -> new RuntimeException("Client not found: " + clientRole.getClientId()));

			RolesResource rolesResource = realmResource.clients().get(client.getId()).roles();

			clientRole.getRoles().stream().map(this::mapRoleRep).forEach(rolesResource::create);
		}
	}
	
//	public Set<RoleRepresentation> getOverAllRolesForClientId(String clientId) {
//	    Set<RoleRepresentation> allRoles = new HashSet<>();
//	    allRoles.addAll(getAllRoleByClientId(clientId)); 
//	    allRoles.addAll(realmRoleKcServices.getListOfRole());
//	    return allRoles;
//	}

	public Set<String> getOverAllRolesForClientId(String clientId) {
	    Set<String> allRoleNames = new HashSet<>();

	    getAllRoleByClientId(clientId).forEach(role -> allRoleNames.add(role.getName())); 
	    realmRoleKcServices.getListOfRole().forEach(role -> allRoleNames.add(role.getName())); 

	    return allRoleNames;
	}

	
	public List<RoleRepresentation> getAllRoleByClientId(String clientId) {
		RealmResource realmResource = getRealmResource();
		ClientRepresentation client = realmResource.clients().findByClientId(clientId).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

		return realmResource.clients().get(client.getId()).roles().list();
	}
	
	public void updateRoleByClientId(String clientId, String roleName, Roles updateRole) {
		RealmResource realmResource = getRealmResource();

		// Find client
		List<ClientRepresentation> clients = realmResource.clients().findByClientId(clientId);
		
		if (clients.isEmpty())
			return; 

		ClientRepresentation client = clients.get(0);
		RolesResource rolesResource = realmResource.clients().get(client.getId()).roles();

		// Check if role exists before updating
		List<RoleRepresentation> roles = rolesResource.list();
		Optional<RoleRepresentation> existingRole = roles.stream().filter(role -> role.getName().equals(roleName)).findFirst();

		if (existingRole.isPresent()) {
			RoleRepresentation roleRep = mapRoleRep(updateRole);
			rolesResource.get(roleName).update(roleRep);
		}else {
			throw new RuntimeException("Role does not exists: " + roleName);
		}
	}

	
	public void deleteRoleByClientId(String clientId, String roleName) {
		RealmResource realmResource = getRealmResource();

		// Find the client by clientId
		ClientRepresentation client = realmResource.clients().findByClientId(clientId).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

		RolesResource rolesResource = realmResource.clients().get(client.getId()).roles();

		// Check if role exists before deleting
		try {
			rolesResource.get(roleName).remove();
		} catch (Exception e) {
			throw new RuntimeException("Role not found: " + roleName);
		}
	}

	
	
	private RealmResource getRealmResource() {
		return keycloakUtil.getRealmResource();
	}
	
	private RoleRepresentation mapRoleRep(Roles role) {
	    RoleRepresentation roleRep = new RoleRepresentation();
	    roleRep.setName(role.getName());
	    roleRep.setDescription(role.getDesc());

	    // Convert attributes from List<AttributeDTO> to Map<String, List<String>>
	    if (role.getAttributes() != null && !role.getAttributes().isEmpty()) {
	        Map<String, List<String>> attributesMap = role.getAttributes()
	            .stream()
	            .collect(Collectors.toMap(
	            		AttributeDTO::getKey,
	                attr -> attr.getValue() // Ensure value is stored as a List<String>
	            ));
	        roleRep.setAttributes(attributesMap);
	    }

	    return roleRep;
	}
}
