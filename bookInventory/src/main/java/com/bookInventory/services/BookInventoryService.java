package com.bookInventory.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bookInventory.modal.BookInventory;
import com.bookInventory.repository.BookInventoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookInventoryService {

	private final BookInventoryRepository bookInventoryRepo;

	public List<BookInventory> createBookInventory(Set<BookInventory> bookInventory) {
		return bookInventoryRepo.saveAll(bookInventory);
	}
	
	public Set<Map<String, ?>> bookInvnetory() {
		List<BookInventory> bookInvntory = bookInventoryRepo.findAll();
		
		return bookInvntory.stream().collect(Collectors.groupingBy(BookInventory::getAuthorName)).entrySet().stream().map(bookInv -> {
			String authorName = bookInv.getKey();
			List<BookInventory> bookInventoryData = bookInv.getValue();
			
			Map<String, Long> bookCount = bookInventoryData.stream().collect(Collectors.groupingBy(BookInventory::getBookName, Collectors.counting()));
			
			 Map<String, Object> response = new HashMap<>();
			 response.put("authorName", authorName);
			 response.put("bookCount", bookCount);
			 response.put("bookInventory", bookInventoryData);
               
			 return response;

		}).collect(Collectors.toSet());
		
		
	}

}
