package com.epam.rd.autocode.assessment.appliances.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        List<String> errors = new ArrayList<>();

        if (password == null)
            errors.add("Password is required");
        if (password.length() < 8)
            errors.add("Password must be at least 8 characters long");
        if (!password.matches(".*[A-Z].*.*[A-Z].*"))
            errors.add("Password must contain at least one uppercase letter");
        if (!password.matches(".*[a-z].*"))
            errors.add("Password must contain at least one lowercase letter");
        if (!password.matches(".*\\d.*"))
            errors.add("Password must contain at least one digit");
        if (!password.matches(".*[!@#$%^&*()].*"))
            errors.add("Password must contain at least one special character");
        if (!errors.isEmpty())
            throw new RuntimeException("SSS");

        return true;
    }
}
