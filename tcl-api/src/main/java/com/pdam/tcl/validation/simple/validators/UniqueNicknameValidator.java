package com.pdam.tcl.validation.simple.validators;

import com.pdam.tcl.service.UserService;
import com.pdam.tcl.validation.simple.anotations.UniqueNickname;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {

    @Autowired
    private UserService service;

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context)  {
        return StringUtils.hasText(nickname) && !service.existsByNickname(nickname);
    }
}
