package com.dev.jwt.jwt.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dev.jwt.jwt.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

    private static final Logger logger = LoggerFactory.getLogger(ExistsByUsernameValidation.class);


    @Autowired
    private UserService userService;


    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        logger.info("Iniciando validación de si el usuario '{}' ya existe", username);
        boolean isValid = !userService.existsByUsername(username);
        if (!isValid) {
            logger.warn("Validación fallida: el usuario '{}' ya existe", username);
        }
        return isValid;
    }
    
}
