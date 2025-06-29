package com.book.network.securityException;

import static com.unicore.constant.SecurityMessages.UNAUTHORIZED;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicore.customeResponse.ErrorResponse;
import com.unicore.customeResponse.ResponseEntityWrapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		ResponseEntityWrapper<?> responseWrapper;	
		responseWrapper = new ResponseEntityWrapper<>(HttpStatus.UNAUTHORIZED.getReasonPhrase(),new ErrorResponse<>(UNAUTHORIZED.value(), UNAUTHORIZED.message(),request.getRequestURI(),null));
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, responseWrapper);
		outputStream.flush();
	}



}
