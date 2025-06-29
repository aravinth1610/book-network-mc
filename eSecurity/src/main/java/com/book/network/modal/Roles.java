package com.book.network.modal;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.book.network.DTO.AttributeDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "auth_role")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Roles extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_role_id")
	private Long pkRoleId;
	
	@Column(name="role_name")
	private String name;
	
	@Column(name="role_desc")
	private String desc;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_role", insertable = false, updatable = false)
	@JsonBackReference
	private AuthRoutes authRoutes;
	
	@Transient
	private List<AttributeDTO> attributes;

	@Override
	public int hashCode() {
		return Objects.hash(pkRoleId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Roles other = (Roles) obj;
		return Objects.equals(pkRoleId, other.pkRoleId);
	}
	
	
	
	
}
