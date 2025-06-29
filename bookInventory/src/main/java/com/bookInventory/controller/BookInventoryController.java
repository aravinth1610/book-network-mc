package com.bookInventory.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookInventory.modal.BookInventory;
import com.bookInventory.services.BookInventoryService;
import com.unicore.customeResponse.ResponseEntityWrapper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class BookInventoryController {

	private final BookInventoryService bookServices;

	@PostMapping
	public ResponseEntityWrapper<?> createBookInv(@RequestBody Set<BookInventory> bookInventory) {
		return new ResponseEntityWrapper<>(bookServices.createBookInventory(bookInventory));
	}
	
	@GetMapping
	public ResponseEntityWrapper<?> createBookInv() {
		return new ResponseEntityWrapper<>(bookServices.bookInvnetory());
	}
	

}
