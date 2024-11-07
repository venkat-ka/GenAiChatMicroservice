package com.genaichat.message.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.genaichat.message.shared.UserDto;


public interface UsersService extends UserDetailsService {
	UserDto createUser(UserDto userDetails);
	UserDto getUserDetailsByEmail(String email);
	UserDto getUserByUserId(String userId, String authorization);
	List<UserDto> getAllUser();
}
