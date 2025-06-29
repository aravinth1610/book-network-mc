package com.book.network.DTO;

import java.util.Set;

import com.book.network.modal.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRoles {

	private String clientId;

	private Set<Roles> roles;
}
