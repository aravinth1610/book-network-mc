package com.booknetwork.config;
 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
 
import com.unicore.customeExceptions.CommonCaseException;
import com.unicore.customeExceptions.UnauthorizedException;
 
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
 
@Component
@AllArgsConstructor
public class WebClientCall {
 
	private final WebCallConfig webCall;
 
	public <T, R> R makeAPIClientCalls(String path, T requestBody, Class<R> responseData, HttpHeaders httpHeaders, String httpMethod) {
 
		HttpMethod method = HttpMethod.valueOf(httpMethod.toUpperCase());
		if (method == null) {
			throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
		}
 
		WebClient.RequestBodySpec requestSpec = webCall.webclientCallConfig().build().method(method).uri(path)
				.headers(headers -> headers.addAll(httpHeaders));
		WebClient.ResponseSpec responseSpec;
 
		if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
			responseSpec = requestSpec.bodyValue(requestBody).retrieve();
		} else {
			responseSpec = requestSpec.retrieve();
		}
		return responseSpec.onStatus(a -> a.isError(), this::handleError).bodyToMono(responseData).block();
	}
 
	public <T, R> Mono<R> makeAsyncAPIClientCalls(String path, T requestBody, Class<R> responseData, HttpHeaders httpHeaders, String httpMethod) {

	    HttpMethod method = HttpMethod.valueOf(httpMethod.toUpperCase());
	    if (method == null) {
	        return Mono.error(new IllegalArgumentException("Unsupported HTTP method: " + httpMethod));
	    }

		WebClient.RequestBodySpec requestSpec = webCall.webclientCallConfig().build().method(method).uri(path)
				.headers(headers -> headers.addAll(httpHeaders));

	    WebClient.ResponseSpec responseSpec;

	    if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
	        responseSpec = requestSpec.bodyValue(requestBody).retrieve();
	    } else {
	        responseSpec = requestSpec.retrieve();
	    }
	    
		return responseSpec.onStatus(a -> a.isError(), this::handleError).bodyToMono(responseData);
	}

	
	private Mono<Throwable> handleError(ClientResponse response) {
		if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
			throw new UnauthorizedException(
					"Web Client Call, Authentication is required and has failed or has not yet been provided.");
		} else if (response.statusCode() == HttpStatus.FORBIDDEN) {
			throw new RuntimeException("Web Client Call You need to be logged in to access this resource.");
		} else if (response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new RuntimeException("Web Client Call An error occurred while processing your request.");
		}
		throw new CommonCaseException("Web Client Call Non-success response received");
	}
 
}