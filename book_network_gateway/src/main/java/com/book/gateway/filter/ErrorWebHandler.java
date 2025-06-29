package com.book.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
@Order(-2) // Ensure it runs before the default error handler
public class ErrorWebHandler implements ErrorWebExceptionHandler {

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		// need to keep logger

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		String dateTime = formatter.format(new Date());

		String request = exchange.getRequest().getURI().getPath();
		HttpStatusCode status = ((ResponseStatusException) ex).getStatusCode();
		String reason = "Internal Server Error";
		String message = ((ResponseStatusException) ex).getMessage();
	
		if (ex instanceof ResponseStatusException) {

			if (status == HttpStatus.NOT_FOUND) {
				reason = "Not Found";
				message = "Resource not found.";

			} else if (status == HttpStatus.SERVICE_UNAVAILABLE) {
				reason = "SERVICE_UNAVAILABLE";
				message = "Services will be Up or Check the Base Path.";
			}
		}

		String errorMessage = String.format("{\"status\": \"Failure\",\"message\": \"%s\",\"data\": [{\"errorCode\": \"ABI002\",\"reason\": \"%s\",\"schemaPath\": \"%s\",\"timestamp\": \"%s\"}]}", message, reason, request, dateTime);

		exchange.getResponse().setStatusCode(status);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

		return exchange.getResponse().writeWith(
				Mono.just(exchange.getResponse().bufferFactory().wrap(errorMessage.getBytes(StandardCharsets.UTF_8))));
	}

}
