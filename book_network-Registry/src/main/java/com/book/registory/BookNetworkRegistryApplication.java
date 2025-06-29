package com.book.registory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BookNetworkRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkRegistryApplication.class, args);
	}

}
