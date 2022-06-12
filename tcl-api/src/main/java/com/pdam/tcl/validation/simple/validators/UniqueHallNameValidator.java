package com.pdam.tcl.validation.simple.validators;


import com.pdam.tcl.service.HallService;
import com.pdam.tcl.validation.simple.anotations.UniqueHallName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueHallNameValidator implements ConstraintValidator<UniqueHallName, String> {

    @Autowired
    private HallService service;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
            return StringUtils.hasText(name) && !service.existsByName(name);
            }

}
