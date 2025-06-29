package com.book.network.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.book.network.modal.AuthRoutes;
import com.book.network.repositoryDTO.SecurityConfigRepositoryDTO;

import jakarta.persistence.QueryHint;

@Repository
public interface AuthMenuRepository extends JpaRepository<AuthRoutes, Long> {

	@QueryHints({ @QueryHint(name = "org.hibernate.readOnly", value = "true"),
	    @QueryHint(name = "org.hibernate.fetchSize", value = "50"),
	    @QueryHint(name = "jakarta.persistence.query.timeout", value = "5000"),
//		@QueryHint(name = "org.hibernate.cacheable", value = "true"), // To store in hibernate cache
//		@QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "USE"),  //Using Second Level Cache (KEYS: USE, BYPASS)
//		@QueryHint(name = "jakarta.persistence.cache.storeMode", value = "USE")  //Using to store Second Level Cache (KEYS: USE, BYPASS, REFRESH)
})
	@Query("SELECT p.pkAuthRoute AS pkAuthRoute,p.apiEndPoint AS apiEndPoint, p.permission AS permission,p.canActivate AS canActivate,r AS roles FROM AuthRoutes p LEFT JOIN p.roles r")
	Set<SecurityConfigRepositoryDTO> getDataVal();
	
}
