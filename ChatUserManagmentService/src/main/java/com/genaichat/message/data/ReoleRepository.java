package com.genaichat.message.data;

import org.springframework.data.repository.CrudRepository;

public interface ReoleRepository extends CrudRepository<RoleEntity, Long> {
	RoleEntity findByName(String name);
}
