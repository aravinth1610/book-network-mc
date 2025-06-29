package com.book.network.modal;

import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "auth_routes")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRoutes extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_authroute")
	private Long pkAuthRoute;

	private String path;
	
	@Column(name = "redirect_to")
	private String redirectTo;
	
	@Column(name = "path_match")
	private String pathMatch;
	
	private String component;
	
	@Column(name = "can_activate")
	private String canActivate;
	
	@Column(name = "can_deactivate")
	private String canDeactivate;

	@Column(name = "api_end_point")
	private String apiEndPoint;
	
	private String permission;

	@OneToMany(targetEntity = AuthRoutes.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "fk_parent_auth_route")
	@JsonManagedReference
	private Set<AuthRoutes> childrenAuthRoute;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_parent_auth_route", insertable = false, updatable = false)
	@JsonBackReference
	private AuthRoutes parentAuthRoute;
	
	@OneToMany(targetEntity = Roles.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "fk_role")
	@JsonManagedReference
	private Set<Roles> roles;

	@Override
	public int hashCode() {
		return Objects.hash(pkAuthRoute);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthRoutes other = (AuthRoutes) obj;
		return Objects.equals(pkAuthRoute, other.pkAuthRoute);
	}

}
