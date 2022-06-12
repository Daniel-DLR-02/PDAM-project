package com.pdam.tcl.validation.simple.anotations;

import com.pdam.tcl.validation.simple.validators.UniqueNicknameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNicknameValidator.class)
@Documented
public @interface UniqueNickname {

    String message() default "El nickname de usuario no puede estar ya en uso.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
