package com.genaichat.message.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface UsersRepository extends CrudRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);
	UserEntity findByUserId(String userId);
	List<UserEntity> findAll();
}
