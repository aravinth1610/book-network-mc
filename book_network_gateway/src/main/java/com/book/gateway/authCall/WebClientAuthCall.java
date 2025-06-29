package com.book.gateway.authCall;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientAuthCall {

	@Bean
	@LoadBalanced
	WebClient.Builder webClientBuild() {
		return WebClient.builder();
	}

	public Mono<Void> tokenValidationAPIExchange(HttpHeaders httpHeaders, ServerWebExchange exchange,
			GatewayFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();
		return webClientBuild().build().post()
				.uri("lb://esecurity/esecurity/validate")
			        .headers(header -> header.addAll(httpHeaders))
			        .retrieve()
			        .bodyToMono(Map.class)
				.flatMap(responseMap -> {
					Map<String, Object> responseData = (Map<String, Object>) responseMap.get("data");

					ServerHttpRequest mutatedRequest = request.mutate()
					        .headers(gatewayHttpHeaders -> {
					        	gatewayHttpHeaders.add("X-UserID-X", responseData != null ? responseData.get("X-user-X-Id").toString() : "unknown");
					        })
					        .build();


					return chain.filter(exchange.mutate().request(mutatedRequest).build());
				}).onErrorResume(error -> {
					HttpStatusCode errorCode;
					String errorMsg;
					if (error instanceof WebClientResponseException) {

						WebClientResponseException webClientException = (WebClientResponseException) error;
						errorCode = webClientException.getStatusCode();
						String responseErrorMsg = webClientException.getResponseBodyAsString();

						if (responseErrorMsg.contains("LoadBalancer does not contain an instance for the service")) {
						        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
						        String dateTime = formatter.format(new Date());
							errorMsg = "{\"status\": \"Failure\",\"message\": \"Service Unavailable.\",\"data\": [{\"errorCode\": \"ABI003\",\"reason\": \"No available instances for esecurity.\",\"schemaPath\": \"" + request.getURI().getPath() + "\",\"timestamp\": \"" + dateTime + "\"}]}";
						} else {
							errorMsg = responseErrorMsg;
						}

					} else {
						errorCode = HttpStatus.BAD_GATEWAY;
						errorMsg = HttpStatus.BAD_GATEWAY.getReasonPhrase();
					}

					return onError(exchange, String.valueOf(errorCode.value()), errorMsg, errorCode);
				});
	}

	private Mono<Void> onError(ServerWebExchange exchange, String errCode, String err,HttpStatusCode httpStatus) {
		
		DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		try {
			response.getHeaders().add("Content-Type", "application/json");
			byte[] bytes = err.getBytes();
			return response.writeWith(Mono.just(bytes).map(t -> dataBufferFactory.wrap(t)));

		} catch (Exception e) {
			e.printStackTrace();

		}
		return response.setComplete();
	}

}