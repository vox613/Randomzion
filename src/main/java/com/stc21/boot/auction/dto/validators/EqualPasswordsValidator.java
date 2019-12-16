package com.stc21.boot.auction.dto.validators;

import com.stc21.boot.auction.dto.UserRegistrationDto;
import com.stc21.boot.auction.dto.validators.annotations.EqualPasswords;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswords, UserRegistrationDto> {

    @Override
    public void initialize(EqualPasswords constraint) {
    }

    @Override
    public boolean isValid(UserRegistrationDto form, ConstraintValidatorContext context) {
        return form.getPassword().equals(form.getRepeatPassword());
    }

}