package com.dev.jwt.jwt.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy=ExistsByUsernameValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  ExistsByUsername {

    /*message error - db */
    String message() default "{User exist in db}";

    Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};

}
