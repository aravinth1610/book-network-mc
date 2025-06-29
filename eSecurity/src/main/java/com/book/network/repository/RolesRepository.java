package com.book.network.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.book.network.modal.Roles;

import jakarta.persistence.QueryHint;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
	
	@QueryHints({ @QueryHint(name = "org.hibernate.readOnly", value = "true"),
		    @QueryHint(name = "org.hibernate.fetchSize", value = "50"),
		    @QueryHint(name = "jakarta.persistence.query.timeout", value = "5000"),
//			@QueryHint(name = "org.hibernate.cacheable", value = "true"), // To store in hibernate cache
//			@QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "USE"),  //Using Second Level Cache (KEYS: USE, BYPASS)
//			@QueryHint(name = "jakarta.persistence.cache.storeMode", value = "USE")  //Using to store Second Level Cache (KEYS: USE, BYPASS, REFRESH)
	})
	@Query("SELECT r.pkRoleId FROM Roles r WHERE r.name=:roleName")
	List<Long> existsByRoleName(@Param(value = "roleName") List<String> roleName);

}
