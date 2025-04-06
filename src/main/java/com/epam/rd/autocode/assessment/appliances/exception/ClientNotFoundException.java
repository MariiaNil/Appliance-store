package com.epam.rd.autocode.assessment.appliances.exception;

public class ClientNotFoundException extends ResourceNotFoundException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
