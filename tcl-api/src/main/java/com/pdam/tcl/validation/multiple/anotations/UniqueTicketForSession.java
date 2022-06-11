package com.pdam.tcl.validation.multiple.anotations;


import com.pdam.tcl.validation.multiple.validators.UniqueTicketForSessionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Constraint(validatedBy = UniqueTicketForSessionValidator.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTicketForSession {

    String message() default "Este sitio ya esta ocupado";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String sessionUuid();

    int row();

    int column();


}
