package com.pdam.tcl.validation.simple.anotations;

import com.pdam.tcl.validation.simple.validators.UniqueHallNameValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueHallNameValidator.class)
@Documented
public @interface UniqueHallName {

    String message() default "No puede haber dos salas con el mismo nombre.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}