package com.book.network.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import com.book.network.modal.AuthOrgConfig;
import com.book.network.modal.AuthRoutes;
import com.book.network.repository.AuthMenuRepository;
import com.book.network.repository.AuthOrgConfigRepository;
import com.book.network.repositoryDTO.SecurityConfigRepositoryDTO;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthMenuServices {

	private final AuthMenuRepository authMenuRepo;

	private final RealmRoleKcServices roleKcServices;
	
	private final AuthOrgConfigRepository authOrgConfigRepository;
	
//	private final RolesRepository roleRepository;

	@Transactional
	public List<AuthRoutes> createAuthRoutes(List<AuthRoutes> menu) {
		try {
			Set<String> existingRoleNames = roleKcServices.getListOfRole().stream().map(RoleRepresentation::getName)
					.collect(Collectors.toSet());

			menu.forEach(route -> {
				if (route.getRoles() != null) { // Ensure route is not null
					processAllRoles(route, existingRoleNames);
				}
			});
		} catch (Exception e) {
			System.out.println("ROLE DOEST NOT ADDED ");
		}
		return authMenuRepo.saveAll(menu); // Filter out null values
	}


	public List<AuthRoutes> getListOfAuthRoutes() {
		List<AuthRoutes> authRoutes = authMenuRepo.findAll();
		return authRoutes;
	}
	
	public Set<SecurityConfigRepositoryDTO> getSecurityConfigPermission(){
		return authMenuRepo.getDataVal();
	}
	
	public AuthOrgConfig authOrgConfig(AuthOrgConfig auth) {
		return authOrgConfigRepository.save(auth);
	}
	
	
	private void processAllRoles(AuthRoutes route, Set<String> existingRoleNames) {
	    // Process roles for current route and all children in one pass
	    Stream.concat(
	            Stream.of(route), // Current route
	            route.getChildrenAuthRoute() != null ? route.getChildrenAuthRoute().stream() : Stream.empty() // Child routes
	    )
	    .flatMap(r -> r.getRoles().stream()) // Get all roles
	    .filter(role -> !existingRoleNames.contains(role.getName())) // Filter out existing roles
	    .forEach(roleKcServices::createRole); // Create roles
	}

}
