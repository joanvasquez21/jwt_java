package com.dev.jwt.jwt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.jwt.jwt.entities.Role;
import com.dev.jwt.jwt.entities.User;
import com.dev.jwt.jwt.repositories.RoleRepository;
import com.dev.jwt.jwt.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>)userRepository.findAll();
    }
    @Override
    @Transactional
    public User save(User user) {
        System.out.println("Guardando usuario: " + user.getUsername());
    
        Optional<Role> optionalRoleUser = roleRepository.findByName("usuario");
        List<Role> roles = new ArrayList<>();
    
        if (optionalRoleUser.isPresent()) {
            roles.add(optionalRoleUser.get());
            System.out.println("Rol 'usuario' asignado");
        } else {
            System.out.println("Rol 'usuario' no encontrado");
        }

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("admin");
            if (optionalRoleAdmin.isPresent()) {
                roles.add(optionalRoleAdmin.get());
                System.out.println("Rol 'admin' asignado");
            } else {
                System.out.println("Rol 'admin' no encontrado");
            }
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
    
        User savedUser = userRepository.save(user);
        System.out.println("Usuario guardado con ID: " + savedUser.getId() + " y roles: " + savedUser.getRoles());
    
        return savedUser;
    }
}
