package com.book.network.keycloakConfig;

import java.util.Optional;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.book.network.modal.AuthOrgConfig;
import com.book.network.repository.AuthOrgConfigRepository;

@Component
public class KeycloakSecurityUtil {

	@Value("${esecurity.kc.serverURI}")
	private String serverUrl;

	@Value("${esecurity.kc.realm}")
	private String realm;

	private String clientId;

	private String clientSecret;
	
	private final AuthOrgConfigRepository authOrgConfigRepsitory;
	
	public KeycloakSecurityUtil( AuthOrgConfigRepository authOrgConfigRepsitory ) {
		super();
		this.authOrgConfigRepsitory = authOrgConfigRepsitory;
	}

	public void initKeycloak(Long orgId) {
		Optional<AuthOrgConfig> authOrgConfig = authOrgConfigRepsitory.findByOrgId(orgId);
		if(authOrgConfig.isEmpty()) {
			throw new RuntimeException("No Keycloak configuration found for orgId: " + orgId);		
		}	
		
		this.clientId = authOrgConfig.get().getClientId();
		this.clientSecret = authOrgConfig.get().getClientSecret();
	}

	public Keycloak getKeycloakInstance() {
		System.out.println("--------------KC");
		return KeycloakBuilder.builder().serverUrl(serverUrl).grantType(OAuth2Constants.CLIENT_CREDENTIALS)
				.realm(realm).clientId(clientId).clientSecret(clientSecret).build();
	}

	public String getRealm() {
		return this.realm;
	}

	public ClientsResource getClientResource() {
		Keycloak keycloak = getKeycloakInstance();
		return keycloak.realm(this.realm).clients();
	}
	
	public RealmResource getRealmResource() {
		Keycloak keycloak = getKeycloakInstance();
		return keycloak.realm(this.realm);
	}
	
	public RolesResource getRolesResource() {
		Keycloak keycloak = getKeycloakInstance();
		RealmResource realmResourse = keycloak.realm(this.realm);
		return realmResourse.roles();
	}



	
}
