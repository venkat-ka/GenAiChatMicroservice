package com.genaichat.message;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	
	@Autowired
	Environment env;
	
	public AuthorizationHeaderFilter() {
		super(Config.class);
	}
	
	public static class Config {
		private List<String> adminAuthorities = Arrays.asList("DELETE","READ","ROLE_ADMIN", "WRITE");
		private List<String> userAuthorities = Arrays.asList("DELETE","READ","ROLE_USER", "WRITE");
//		private String role;
//		private String authority;

		public List<String> getAdminAuthorities() {
			return adminAuthorities;
		}

		public void setAdminAuthorities(String authorities) {
			this.adminAuthorities = Arrays.asList(authorities.split(" "));
		}
		
		public List<String> getUserAuthorities() {
			return userAuthorities;
		}

		public void setUserAuthorities(String authorities) {
			this.userAuthorities = Arrays.asList(authorities.split(" "));
		}

//		private String getRole() {
//			return role;
//		}
//
//		public void setRole(String role) {
//			this.role = role;
//		}
//
//		private String getAuthority() {
//			return authority;
//		}
//
//		public void setAuthority(String authority) {
//			this.authority = authority;
//		}
		
	}
	
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("authorities");
	}
	
//	@Override
//	public List<String> shortcutFieldOrder() {
//		return Arrays.asList("role","authority");
//	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			
			ServerHttpRequest request = exchange.getRequest();
			
			if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No authorization header",HttpStatus.UNAUTHORIZED);
			}
			
			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorizationHeader.replace("Bearer", "").trim();
			
			List<String> authorities = getAuthorities(jwt);
			
	        boolean hasRequiredAdminAuthority = authorities.stream()
	        		.anyMatch(authority->config.getAdminAuthorities().contains(authority));
	        
	        boolean hasRequiredUserAuthority = authorities.stream()
	        		.anyMatch(authority->config.getUserAuthorities().contains(authority));
	        
	        if(!hasRequiredAdminAuthority && !hasRequiredUserAuthority) 
	        	return onError(exchange,"User is not authorized to perform this operation", HttpStatus.FORBIDDEN);
	        
//			if(!isJwtValid(jwt)) {
//				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
//			}
			
			return chain.filter(exchange);
		};
	}
	
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        
        DataBufferFactory bufferFactory = response.bufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(err.getBytes());
        
        return response.writeWith(Mono.just(dataBuffer));
    }
    
	private List<String> getAuthorities(String jwt) {
		List<String> returnValue = new ArrayList<>();

		String tokenSecret = env.getProperty("token.secret");
		byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
		SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

		JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

		try {

			Jws<Claims> parsedToken = parser.parseSignedClaims(jwt);
			List<Map<String, String>> scopes = ((Claims)parsedToken.getPayload()).get("scope", List.class);
			scopes.stream().map(scopeMap -> returnValue.add(scopeMap.get("authority"))).collect(Collectors.toList());
			System.out.println(returnValue);
			System.out.println("Authorized Role trigger");

		} catch (Exception ex) {
			return returnValue;
		}


		return returnValue;
	}

}
