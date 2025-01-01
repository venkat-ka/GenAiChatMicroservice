package com.genaichat.message.data;


import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.genaichat.message.shared.Roles;

import jakarta.transaction.Transactional;


@Component
public class InitialUsersSetup {
	
	@Autowired
	AutorityRepository authorityRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UsersRepository usersRepository;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transactional
	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.info("From Application Ready Event");
		
		AuthorityEntity readAuthority = createAuthority("READ");
		AuthorityEntity writeAuthority = createAuthority("WRITE");
		AuthorityEntity deleteAuthority = createAuthority("DELETE");
		
		createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
		RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

		
		RoleEntity rcAdmin = createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
		
		
		if(roleAdmin == null) return;
		
		UserEntity adminUser = new UserEntity();
		adminUser.setFirstName("Venkat");
		adminUser.setLastName("Krishna");
		adminUser.setEmail("admin@test.com");
		adminUser.setUserId(UUID.randomUUID().toString());
		adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		adminUser.setRoles(Arrays.asList(roleAdmin));
		
		UserEntity storedAdminUser = usersRepository.findByEmail("admin@test.com").orElseGet(()->usersRepository.save(adminUser));
		
		UserEntity rcUser = new UserEntity();
		rcUser.setFirstName("rciverVenkat");
		rcUser.setLastName("rckrish");
		rcUser.setEmail("rcvr@test.com");
		rcUser.setUserId(UUID.randomUUID().toString());
		rcUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		rcUser.setRoles(Arrays.asList(rcAdmin));
		
		/* Reciever user */
		
		UserEntity storedRecieverUser = usersRepository.findByEmail("rcvr@test.com").orElseGet(()->usersRepository.save(rcUser));
		
		/* End */
//		if(storedAdminUser.get() == null) {
//			usersRepository.save(adminUser);
//		}
		
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) {
		
		AuthorityEntity authority = authorityRepository.findByName(name);
		
		if(authority == null) {
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
		}
		
		return authority;
	}
	
	@Transactional
	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
		
		RoleEntity role = roleRepository.findByName(name);
		
		if(role == null) {
			role = new RoleEntity(name, authorities);
			roleRepository.save(role);
		}
		
		return role;
		
	}
}
