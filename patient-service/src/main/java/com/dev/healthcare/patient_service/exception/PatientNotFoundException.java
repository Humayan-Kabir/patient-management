package com.dev.healthcare.patient_service.exception;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String message) {
        super(message);
    }
}
