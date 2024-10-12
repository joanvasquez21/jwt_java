package com.dev.jwt.jwt.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dev.jwt.jwt.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    
}
