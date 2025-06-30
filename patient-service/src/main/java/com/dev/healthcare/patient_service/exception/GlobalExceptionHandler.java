package com.dev.healthcare.patient_service.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExist(
        EmailAlreadyExistException ex) {
        logger.warn(ex.getMessage());

        return ResponseEntity.badRequest().body(Map.of("message", "Email address already exists"));
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePatientNotFound(PatientNotFoundException ex) {
        logger.warn(ex.getMessage());

        return ResponseEntity.badRequest().body(Map.of("message", "Patient not found"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex) {

        logger.warn("Request parsing error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException ife) {
            String fullPath = buildFullPath(ife.getPath());
            logger.warn("Invalid format for field: {}", fullPath);
            errors.put(fullPath, "Invalid format");
        }

        return ResponseEntity.badRequest().body(errors);
    }

    private String buildFullPath(List<Reference> path) {
        if (path == null || path.isEmpty()) {
            return "unknown";
        }
        StringJoiner joiner = new StringJoiner(".");
        for (JsonMappingException.Reference ref : path) {
            if (ref.getFieldName() != null) {
                joiner.add(ref.getFieldName());
            }
            // Handle array indices: contact.phones[0]
            else if (ref.getIndex() >= 0) {
                String last = joiner.length() > 0
                    ? joiner.toString()
                    : "array";
                return last + "[" + ref.getIndex() + "]";
            }
        }
        return joiner.toString();
    }
}