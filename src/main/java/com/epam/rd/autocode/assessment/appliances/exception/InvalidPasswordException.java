package com.epam.rd.autocode.assessment.appliances.exception;

import java.util.Collections;
import java.util.List;

public class InvalidPasswordException extends RuntimeException {

    private final List<String> validationErrors;
    public InvalidPasswordException(List<String> errors) {
        super("Invalid password: " + String.join(", ", errors));
        this.validationErrors = errors;
    }

    public InvalidPasswordException(String message) {
        super(message);
        this.validationErrors = Collections.singletonList(message);
    }

    // Геттер для получения списка ошибок
    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
