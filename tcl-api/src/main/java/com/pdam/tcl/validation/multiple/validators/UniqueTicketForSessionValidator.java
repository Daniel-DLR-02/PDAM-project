package com.pdam.tcl.validation.multiple.validators;

import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.validation.multiple.anotations.UniqueTicketForSession;
import com.pdam.tcl.validation.simple.anotations.UniqueEmail;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UniqueTicketForSessionValidator implements ConstraintValidator<UniqueTicketForSession, Object> {

    private String sessionUuid;
    private int row;
    private int column;

    @Autowired
    private SessionService sessionService;

    @Override
    public void initialize(UniqueTicketForSession constraintAnnotation) {
        this.sessionUuid = constraintAnnotation.sessionUuid();
        this.row = constraintAnnotation.row();
        this.column = constraintAnnotation.column();


    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        return sessionService.isOccupied(UUID.fromString(sessionUuid),row,column);
    }


}
