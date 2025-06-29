package com.book.network.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import com.book.network.DTO.AttributeDTO;
import com.book.network.keycloakConfig.KeycloakSecurityUtil;
import com.book.network.modal.Roles;

@Service
public class RealmRoleKcServices {
	
	private final KeycloakSecurityUtil keycloakUtil;
	
	private static final int PAGE_SIZE = 50; // Fetch roles in batches

	public RealmRoleKcServices(KeycloakSecurityUtil keycloakUtil) {
		super();
		this.keycloakUtil = keycloakUtil;
	}

	public void createRole(Roles role) {
		RoleRepresentation roleRep = mapRoleRep(role);
		getRolesResource().create(roleRep);
	}

	public RoleRepresentation getRole(String roleName) {
		return getRolesResource().get(roleName).toRepresentation();
	}

	public List<RoleRepresentation> getListOfRole() {
		return getRolesResource().list();
	}

	public void updateRole(String roleName,Roles role) {
		RoleRepresentation roleRep = mapRoleRep(role);
		getRolesResource().get(roleName).update(roleRep);
	}

	public void deleteUser(String roleName) {
		getRolesResource().deleteRole(roleName);
	}
	
	
	//Based on Batch Size
	 public Set<RoleRepresentation> getRolesByAttributeValues(List<String> attributeValues) {
	        RolesResource rolesResource = getRolesResource();
	        Set<String> valueSet = Set.copyOf(attributeValues); // Use HashSet for fast lookup
	        Set<RoleRepresentation> matchedRoles = new HashSet<>();
	        int first = 0;

	        while (true) {
	            List<RoleRepresentation> roles = rolesResource.list(first, PAGE_SIZE);
	            if (roles.isEmpty()) { 
	            	break; // Stop fetching if no more roles
	            }

	            matchedRoles.addAll(
	                roles.stream()
	                    .filter(role -> {
	                        Map<String, List<String>> attributes = role.getAttributes();
	                        return attributes != null && attributes.values().stream()
	                                .flatMap(List::stream)
	                                .anyMatch(valueSet::contains);
	                    })
	                    .collect(Collectors.toSet())
	            );

	            first += PAGE_SIZE; // Move to the next batch
	        }

	        return matchedRoles;
	    }

	private RolesResource getRolesResource() {
		return keycloakUtil.getRolesResource();
	}

	private Roles mapRole(RoleRepresentation roleRep) {
		Roles role = new Roles();
		role.setPkRoleId(Long.valueOf(roleRep.getId()));
		role.setName(roleRep.getName());
		return role;
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
