package com.booknetwork.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booknetwork.services.BookServices;
import com.unicore.customeResponse.ResponseEntityWrapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/details")
@AllArgsConstructor
public class BookController {

	private final BookServices bookServices;

	@PostMapping("/{id}")
	public ResponseEntityWrapper<?> getData(@RequestBody Map<String, String> user, @PathVariable("id") String id) { 
		bookServices.bookData(user);
		return new ResponseEntityWrapper<>(Arrays.asList(Map.of("Name", "Ram", "Age", 12, "Books", Arrays.asList("Jemmy", "Makers"))));
	}
	
	@PostMapping
	@CircuitBreaker(name = "bookInv", fallbackMethod = "fallbackGetBookInv")
//	@TimeLimiter(name ="bookInv") Accepts only Asyn
//	@Retry(name = "bookInv")
	public ResponseEntityWrapper<?> getBookInv() { 
		return new ResponseEntityWrapper<>(bookServices.bookInv());
	}
	
	public ResponseEntityWrapper<?> fallbackGetBookInv(RuntimeException throwable) {
		return new ResponseEntityWrapper<>(Arrays.asList(Map.of("Name", "Ram", "Age", 12, "Books", Arrays.asList("Jemmy", "Makers"))));
	}

}
