package com.epam.rd.autocode.assessment.appliances.password;

import com.epam.rd.autocode.assessment.appliances.exception.InvalidPasswordException;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    public static void validate(String password) {
        List<String> errors = new ArrayList<>();

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

        if (!errors.isEmpty()) {
            throw new InvalidPasswordException(errors);
        }
    }
}
