package com.genaichat.message.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.genaichat.message.data.AuthorityEntity;
import com.genaichat.message.data.RoleEntity;
import com.genaichat.message.data.UserEntity;
import com.genaichat.message.data.UsersRepository;
import com.genaichat.message.shared.UserDto;

@Service
public class UserServiceImpl implements UsersService{


	UsersRepository usersRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;

	
	public UserServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<UserEntity> userEntity = Optional.of(usersRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException(username)));
		
	
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Collection<RoleEntity> roles = userEntity.get().getRoles();
		
		roles.forEach((role) -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			
			Collection<AuthorityEntity> authorityEntities = role.getAuthorities();
			authorityEntities.forEach((authorityEntity) -> {
				authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
			});
		});
		
		return new User(userEntity.get().getEmail(), 
				userEntity.get().getEncryptedPassword(), 
				true, true, true, true, 
				authorities);
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Optional<UserEntity> userExstEntity = usersRepository.findByEmail(userDetails.getEmail());
		
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

		usersRepository.save(userEntity);

		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userEntity = Optional.of(usersRepository.findByEmail(email)).orElseThrow(()-> new UsernameNotFoundException(email));
				//.orElseThrow(()-> new UsernameNotFoundException(email));
//
//		if (userEntity == null)
//			throw new UsernameNotFoundException(email);

		return new ModelMapper().map(userEntity.get(), UserDto.class);
	}
	
	@Override
	public UserDto getUserByUserId(String userId, String authorization) {
		
        UserEntity userEntity = usersRepository.findByUserId(userId);     
        if(userEntity == null) throw new UsernameNotFoundException("User not found");
        
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        
        /*
        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
        
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody(); 
        */
        
//        logger.info("Before calling albums Microservice");
//        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId, authorization);
//        logger.info("After calling albums Microservice");
//        
//		userDto.setAlbums(albumsList);
		
		return userDto;
	}

	@Override
	public List<UserDto> getAllUser() {
		// TODO Auto-generated method stub
		List<UserEntity> userEntity = usersRepository.findAll();
		List<UserDto> userDto = new ArrayList<>();
		for(UserEntity user:userEntity) {
			UserDto mapUserDt = new ModelMapper().map(user, UserDto.class);
			userDto.add(mapUserDt);
		}
		return userDto;
	}
	
}