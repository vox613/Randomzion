package com.stc21.boot.auction.dto.validators;


import com.stc21.boot.auction.dto.validators.annotations.ValidPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private Pattern pattern;
    private Matcher matcher;
    private static final String PHONE_NUMBER_PATTERN =
            "^\\+7\\([0-9]{3}\\)[0-9]{3}-[0-9]{2}-[0-9]{2}$";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context){
        return (phoneNumber.equals("") || validateEmail(phoneNumber));
    }

    private boolean validateEmail(String email) {
        pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
