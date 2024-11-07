package com.genaichat.message.data;

import org.springframework.data.repository.CrudRepository;

public interface AutorityRepository extends CrudRepository<AuthorityEntity, Long>  {
	AuthorityEntity findByName(String name);
}
