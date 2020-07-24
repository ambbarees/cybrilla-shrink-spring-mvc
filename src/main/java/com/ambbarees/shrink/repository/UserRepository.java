package com.ambbarees.shrink.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambbarees.shrink.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findById(long id);
	Optional<User> findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
	User findByKeyHash(String keyHash);
}
