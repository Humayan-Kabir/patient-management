package com.dev.healthcare.patient_service.dto;

import com.dev.healthcare.patient_service.dto.validators.CreatePatientValidationGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class PatientRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull(groups = CreatePatientValidationGroup.class, message = "Registered date is required")
    @PastOrPresent(message = "Registered date cannot be in the future")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate registeredDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }
}
