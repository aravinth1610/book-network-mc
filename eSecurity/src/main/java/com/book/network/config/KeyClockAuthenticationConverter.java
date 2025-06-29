package com.book.network.config;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.book.network.keycloakConfig.KeycloakSecurityUtil;
import com.book.network.services.RoleServices;

public class KeyClockAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken>{
	
	private final RoleServices roleServices;
	private final KeycloakSecurityUtil keycloakUtil;
	
	public KeyClockAuthenticationConverter( RoleServices roleServices, KeycloakSecurityUtil keycloakUtil ) {
		super();
		this.roleServices = roleServices;
		this.keycloakUtil = keycloakUtil;
	}
	
	public AbstractAuthenticationToken convert(Jwt jwt) {
		   try {
				return new JwtAuthenticationToken(jwt,
						Stream.concat(new JwtGrantedAuthoritiesConverter().convert(jwt).stream(), resourceRoles(jwt).stream())
								.collect(Collectors.toSet()));
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new RuntimeException("Keycloak token validation failed", e);
	        }
	}

	private Collection<? extends GrantedAuthority> resourceRoles(Jwt jwtToken) {
		System.out.println("--------token");
		Long orgId = getAuthorgId(jwtToken);
		
		keycloakUtil.initKeycloak(orgId);
		System.out.println(getSub(jwtToken)+"-"+getClient(jwtToken));
		Set<String> authorities = getGrantedAuthority(getSub(jwtToken), getClient(jwtToken));

		System.out.println("----Authorities-"+authorities);
		return authorities.stream().map(roleName -> roleName).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
	}
	
	private String getSub(Jwt jwtToken) {
	return jwtToken.getSubject();
	}

	private String getClient(Jwt jwtToken) {
		return jwtToken.getClaim("azp"); 
	}
	
	private Long getAuthorgId(Jwt jwtToken) {
		return jwtToken.getClaim("authOrgId"); 
	}
	
	private Set<String> getGrantedAuthority(String sub, String clientId) {
		return  roleServices.getGrantedAuthority(sub, clientId);
	}

}