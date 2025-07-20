package com.book.network.config;

import java.util.Arrays;
import java.util.function.Supplier;

import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.book.network.modal.Roles;
import com.book.network.services.AuthMenuServices;

import jakarta.servlet.http.HttpServletRequest;
 
@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	private final AuthMenuServices authMenuServices;

	    public DynamicAuthorizationManager(AuthMenuServices authMenuServices) {
	        this.authMenuServices = authMenuServices;
	    }
	    
	    @Override
	    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
	        HttpServletRequest request = context.getRequest();
             System.out.println("---------INNER");
	        try {
	            // Get URI from the context request
	            String contextRequestURI = request.getRequestURI();

	            // Extract values from headers (if present)
	            String headerHttpMethod = request.getHeader("X-Http-X-Method");
	            String headerRequestURI = request.getHeader("X-URI-X-Request");

	            boolean isHeaderPresent = (headerHttpMethod != null || headerRequestURI != null);
	            boolean isContextRequestPresent = (contextRequestURI != null && !contextRequestURI.isEmpty());

	            // If header request is present but context request is missing, deny access
	            if (isHeaderPresent && !isContextRequestPresent) {
	                return new AuthorizationDecision(false);
	            }

//	            Map<String, String> headerRequests = new HashMap<String, String>();
	            
	            return authMenuServices.getSecurityConfigPermission().stream()
	                    .filter(permission -> {
	                        // Extract API Endpoint and Allowed Methods
	                        String apiEndPoint = permission.getApiEndPoint();
	                        String[] methods = permission.getPermission().split(",");

	                        return Arrays.stream(methods)
	                                .map(String::trim)
	                                .map(HttpMethod::valueOf)
	                                .anyMatch(method -> {
	                                    // Match using both AntPathRequestMatcher and Header Path Request Matcher
	                                    AntPathRequestMatcher antMatcher = new AntPathRequestMatcher(apiEndPoint, method.name());
	   
	                                   AntPathMatcher matcher = new AntPathMatcher();
	                                   boolean isHeaderRequest = matcher.match(apiEndPoint, headerRequestURI);
	                                     
										if (isHeaderRequest && headerHttpMethod.equalsIgnoreCase(method.name())) {
												return true;
										} 
										else {
//											if (apiEndPoint.equalsIgnoreCase("/validate") && method.name().equalsIgnoreCase("POST")) {
//												return false;
//											} else {
												return (antMatcher.matches(request));
//											}
										}
	                                });
	                    })
	                    .findFirst()
	                    .map(permission -> {
	                    	System.out.println(permission.getApiEndPoint()+"===="+permission.getCanActivate());
	                      
	                    	if (permission.getCanActivate() == null || permission.getCanActivate().isBlank()) {
	                        	 return new AuthorizationDecision(true);
	                        }
	                    
							if (permission.getRoles() != null && !permission.getRoles().isEmpty()) {
								// Check if user has any required role
								String[] authorities = permission.getRoles().stream().map(Roles::getName)
										.toArray(String[]::new);

								boolean hasAuthority = authentication.get().getAuthorities().stream()
										.anyMatch(grantedAuthority -> Arrays.asList(authorities)
												.contains(grantedAuthority.getAuthority()));

								return new AuthorizationDecision(hasAuthority);
							} else {
								return new AuthorizationDecision(false); // Allow if roles are not required
							}
						}).orElse(new AuthorizationDecision(false));
	        } catch (IllegalArgumentException e) {
	            throw new RuntimeException("Invalid HTTP method in permission: " + e.getMessage(), e);
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to check authorization", e);
	        }
	    }

}