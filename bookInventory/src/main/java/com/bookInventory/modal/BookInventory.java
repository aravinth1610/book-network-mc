package com.bookInventory.modal;

import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "book_inventory")
public class BookInventory extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_book_inventory_id")
	private Long bookInventoryId;

	@Column(name = "book_name")
	private String bookName;

	@Column(name = "authory_name")
	private String authorName;

	@Column(name = "inventory_house")
	private String inventoryHouse;

	@OneToMany(targetEntity = Address.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "fk_address")
	@JsonManagedReference
	private Set<Address> address;

	@Override
	public int hashCode() {
		return Objects.hash(bookInventoryId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookInventory other = (BookInventory) obj;
		return Objects.equals(bookInventoryId, other.bookInventoryId);
	}

}
