package com.dev.healthcare.patient_service.exception;

public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
