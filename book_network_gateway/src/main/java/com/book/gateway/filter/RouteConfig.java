package com.book.gateway.filter;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

	@Bean
	protected RouteLocator routes(RouteLocatorBuilder builder, AuthenticationPreFilter authFilter) {
		return builder.routes()
				.route("esecurity",
						r -> r.path("/esecurity/**")
								.filters(f ->
								//f.rewritePath("/authentication-service(?<segment>/?.*)", "$\\{segment}")
								f.filter(authFilter.apply(new AuthenticationPreFilter.Config())))
								.uri("lb://esecurity"))
				.route("book",
						r -> r.path("/book/**")
								.filters(f -> 
								        //f.rewritePath("/user-service(?<segment>/?.*)", "$\\{segment}")
										f.filter(authFilter.apply(new AuthenticationPreFilter.Config())))
								.uri("lb://book"))
				.route("bookInventory",
						r -> r.path("/bookInventory/**")
								.filters(f -> 
								        //f.rewritePath("/user-service(?<segment>/?.*)", "$\\{segment}")
										f.filter(authFilter.apply(new AuthenticationPreFilter.Config())))
								.uri("lb://bookInventory"))
				.route("book_network-Registry",
						r -> r.path("/eureka/web")
						.filters(f ->  
						f.setPath("/"))
								.uri("http://localhost:9000"))
				.route("discovery-server-static",
						r -> r.path("/eureka/**")
								.uri("http://localhost:9000"))
//				  .route("fallbackRoute", 
//	                        r -> r.alwaysTrue()
//	                              .filters(f -> f.setResponseHeader("Content-Type", "application/json")
//	                                             .rewritePath("/.*", "/")
//	                                             .setStatus(HttpStatus.NOT_FOUND))
//	                              .uri("no://op")) // Use a non-routable URI
				.build();
	}
	
}
