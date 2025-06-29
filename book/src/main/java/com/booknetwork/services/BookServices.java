package com.booknetwork.services;
 
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
 
import com.booknetwork.config.WebClientCall;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 
@Service
@Slf4j
@AllArgsConstructor
public class BookServices {
 
	private final WebClientCall webClientcalls;
	
	
	public void bookData(Map<String,String> bookData) {
		log.info("bookData Args Data {}", bookData);
		System.out.println(bookData);
	}
	
	public Map<String, Object> bookInv() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
		webClientcalls.makeAPIClientCalls("lb://bookInventory/bookInventory/", "", Map.class, httpHeaders, "GET");
		//System.out.println(bookInventoy);

		return null;
	}

//	public Map bookInvAsync() throws InterruptedException, ExecutionException  {
//		HttpHeaders httpHeaders = new HttpHeaders();
//		httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
//	
//		webClientcalls.makeAsyncAPIClientCalls("lb://bookInventory/bookInventory/", "", Map.class, httpHeaders, "GET").subscribe(response -> {	
//			 // success handling
//	        System.out.println("Got response: " + response);
//	    }, error -> {
//	        // error handling
//	        error.printStackTrace();
//	    });
//
//		//Using CompletableFuture
//		CompletableFuture<Map> future = webClientcalls.makeAsyncAPIClientCalls("lb://bookInventory/bookInventory/", "", Map.class, httpHeaders, "GET").toFuture();
//
//		CompletableFuture.allOf(future); // combain all the CompletableFuture
//		
//		Map bookInvCompFuture = future.join(); // wait for the result and return it 
//		System.out.println(bookInvCompFuture);
//
//		
//		//Using Callable
//		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//		Callable<Map> task = () -> webClientcalls
//		    .makeAPIClientCalls("lb://bookInventory/bookInventory/", "", Map.class, httpHeaders, "GET");
//
//		ScheduledFuture<Map> scheduledFuture = scheduler.schedule(task, 0, TimeUnit.SECONDS);
//
//		// Wait and get result (if needed)
//		Map result = scheduledFuture.get(); // blocks until result is ready
//        return result;
//		
//	}




}