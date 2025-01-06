package com.genaichat.message.security;


import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genaichat.message.data.UserEntity;
import com.genaichat.message.data.UsersRepository;
import com.genaichat.message.service.UsersService;
import com.genaichat.message.shared.UserDto;
import com.genaichat.message.ui.model.LoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UsersService usersService;
	private Environment environment;
	
	@Autowired
	public UsersRepository userRepo;
	
	@Autowired
	SimpMessagingTemplate template;
	
	
	public AuthenticationFilter() {
		
	}

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}

	public AuthenticationFilter(UsersService usersService, Environment environment,
			AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.usersService = usersService;
		this.environment = environment;
	}

	public AuthenticationFilter(UsersService usersService, AuthenticationManager authenticationManager, Environment environment, UsersRepository userRepo,
			SimpMessagingTemplate template) {
		super(authenticationManager);
		this.usersService = usersService;
		this.environment = environment;
		this.userRepo = userRepo;
		this.template = template;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {

			LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String userName = ((User) auth.getPrincipal()).getUsername();
		UserDto userDetails = usersService.getUserDetailsByEmail(userName);
		String tokenSecret = environment.getProperty("token.secret");
		byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

		Instant now = Instant.now();

		String token = Jwts.builder()
				.claim("scope", auth.getAuthorities())
				.setSubject(userDetails.getUserId())
				.expiration(
						Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
				.issuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();

		res.addHeader("Access-Token", token);
		res.addHeader("Access-Control-Expose-Headers", "Access-Token, Uid");
		res.addHeader("Uid", userDetails.getUserId());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = userRepo.findByUserId(userDetails.getUserId());
		if(userEntity != null) {
		userEntity.setLoginStatus("y");
		userEntity.setToken(token);
		userRepo.save(userEntity);
		}
		template.convertAndSend("/topic/group", userDetails);
		
	}
}
