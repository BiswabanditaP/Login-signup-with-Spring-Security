package com.biswa.loginrestapi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biswa.loginrestapi.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);
}
