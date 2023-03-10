package com.biswa.loginrestapi.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biswa.loginrestapi.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	   Optional<User> findByEmail(String email);
	    Optional<User> findByUsernameOrEmail(String username, String email);
	    List<User> findByUsername(String username);
	    Boolean existsByUsername(String username);
	    Boolean existsByEmail(String email);

		User findByToken(String token);
}
