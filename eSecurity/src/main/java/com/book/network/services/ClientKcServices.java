package com.book.network.services;

import java.util.Optional;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.stereotype.Service;

import com.book.network.keycloakConfig.KeycloakSecurityUtil;

@Service
public class ClientKcServices {

	private final KeycloakSecurityUtil keycloakUtil;
	
	public ClientKcServices(KeycloakSecurityUtil keycloakUtil) {
		super();
		this.keycloakUtil = keycloakUtil;
	}

	public ClientRepresentation getClientId(String clientId) {
		return getClientResource().findByClientId(clientId).stream().findFirst().get();
	}

	private ClientsResource getClientResource() {
		return keycloakUtil.getClientResource();
	}

}
