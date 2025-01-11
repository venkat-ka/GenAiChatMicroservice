package com.genaichat.message.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genaichat.message.service.UsersService;
import com.genaichat.message.shared.UserDto;
import com.genaichat.message.ui.model.CreateUserRequestModel;
import com.genaichat.message.ui.model.CreateUserResponseModel;
import com.genaichat.message.ui.model.RoleResponseModel;
import com.genaichat.message.ui.model.UserResponseModel;

@RestController
@RequestMapping("/users")
public class UsersController {
	@Autowired
	private Environment env;

	@Autowired
	UsersService usersService;

	ModelMapper modelMapper = new ModelMapper();

	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + env.getProperty("local.server.port");
	}

	@GetMapping(path = "/byemail", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CreateUserResponseModel> getUserByEmail(@RequestParam("email") String email) {
		UserDetails usrDtl = usersService.loadUserByUsername(email);
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		CreateUserResponseModel returnValue = modelMapper.map(usrDtl, CreateUserResponseModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody CreateUserRequestModel userDetails) {

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = usersService.createUser(userDto);

		CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}
	
	@PostMapping(value="/addUser", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CreateUserResponseModel> addUser(@RequestBody CreateUserRequestModel userDetails) {

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = usersService.createUser(userDto);

		CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}

	@GetMapping(value = "/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('ADMIN') or principal == #userId")
	// @PreAuthorize("principal == #userId")
	// @PostAuthorize("principal == returnObject.body.userId")
	public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId,
			@RequestHeader("Authorization") String authorization) {

		UserDto userDto = usersService.getUserByUserId(userId, authorization);
		UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@GetMapping(value = "/role/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<RoleResponseModel> getAuthRole(@PathVariable("userId") String userId){
		UserDto userDto = usersService.getUserByUserId(userId, "authorizedonly");
		List<RoleResponseModel>  roleReturn = new ArrayList<>();
		RoleResponseModel returnValue = new ModelMapper().map(userDto, RoleResponseModel.class);
		roleReturn.add(returnValue);
		return roleReturn;
	}
	
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<UserDto>> getAll(){
		List<UserDto> userDto = usersService.getAllUser();
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}
	
	@PostMapping(value = "/signout")
	public String logout(@RequestBody UserResponseModel userId){
		String usrLogOutId = usersService.userLogout(userId.getUserId());
		
		return usrLogOutId;
	}
	
	
}
