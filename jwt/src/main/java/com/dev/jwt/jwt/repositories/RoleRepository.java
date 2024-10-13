package com.dev.jwt.jwt.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dev.jwt.jwt.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);
    
}
