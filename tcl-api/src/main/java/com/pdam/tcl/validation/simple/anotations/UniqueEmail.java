package com.pdam.tcl.validation.simple.anotations;

import com.pdam.tcl.validation.simple.validators.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {
    String message() default "El email de usuario no puede estar ya en uso.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
