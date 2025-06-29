package com.book.network.securityException;

import static com.unicore.constant.SecurityMessages.FORBIDDEN;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicore.customeResponse.ErrorResponse;
import com.unicore.customeResponse.ResponseEntityWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException {
		ResponseEntityWrapper<?> responseWrapper;	
		responseWrapper = new ResponseEntityWrapper<>( HttpStatus.UNAUTHORIZED.getReasonPhrase(),new ErrorResponse<>(FORBIDDEN.value(), FORBIDDEN.message(),request.getRequestURI(),null));
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, responseWrapper);
		outputStream.flush();
	}
}
