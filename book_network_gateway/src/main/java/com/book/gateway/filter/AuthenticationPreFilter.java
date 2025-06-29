package com.book.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.book.gateway.authCall.WebClientAuthCall;

@Component
public class AuthenticationPreFilter extends AbstractGatewayFilterFactory<AuthenticationPreFilter.Config> {

	public static class Config {}	
	
	@Autowired
	private WebClientAuthCall webClientAuthCall;
	
	
	 @Override
	 public GatewayFilter apply(Config config) {
		 
	        return (exchange, chain) -> {
	            ServerHttpRequest request = exchange.getRequest();	            
	            String fullPath = request.getPath().pathWithinApplication().value();
	            String apiPath = fullPath.substring(fullPath.indexOf("/",1));
	            String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                System.out.println("apiPath===Gateway: "+apiPath+"Method===: "+request.getMethod().toString());
	            HttpHeaders httpHeaders = new HttpHeaders();
	    		httpHeaders.add(HttpHeaders.AUTHORIZATION, bearerToken);
	    		httpHeaders.add("X-URI-X-Request", apiPath);
	    		httpHeaders.add("X-Http-X-Method", request.getMethod().toString());
	    		
	           return  webClientAuthCall.tokenValidationAPIExchange(httpHeaders, exchange,chain);
	        };
	    }
}
