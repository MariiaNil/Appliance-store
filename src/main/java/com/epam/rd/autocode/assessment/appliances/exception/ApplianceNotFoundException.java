package com.epam.rd.autocode.assessment.appliances.exception;

public class ApplianceNotFoundException extends ResourceNotFoundException {
    public ApplianceNotFoundException(String message) {
        super(message);
    }
}
