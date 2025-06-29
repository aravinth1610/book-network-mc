package com.bookInventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.bookInventory", "com.unicore"})
@EnableDiscoveryClient
public class BookInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookInventoryApplication.class, args);
	}

}
