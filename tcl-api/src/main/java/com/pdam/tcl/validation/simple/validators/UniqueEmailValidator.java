package com.pdam.tcl.validation.simple.validators;

import com.pdam.tcl.service.UserService;
import com.pdam.tcl.validation.simple.anotations.UniqueEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService service;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return StringUtils.hasText(email) && !service.existsByEmail(email);
    }
}