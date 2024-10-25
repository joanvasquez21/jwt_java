package com.dev.jwt.jwt.services;

import java.util.List;

import com.dev.jwt.jwt.entities.User;

public interface UserService {
    
    List<User>  findAll();

    User save(User user);
    
    boolean existsByUsername(String username);

}
