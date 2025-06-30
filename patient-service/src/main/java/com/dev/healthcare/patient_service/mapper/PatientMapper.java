package com.dev.healthcare.patient_service.mapper;

import com.dev.healthcare.patient_service.dto.PatientRequestDTO;
import com.dev.healthcare.patient_service.dto.PatientResponseDTO;
import com.dev.healthcare.patient_service.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId().toString());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setDateOfBirth(patient.getDateOfBirth().toString());

        return patientResponseDTO;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        patient.setRegisteredDate(patientRequestDTO.getRegisteredDate());
        return patient;
    }
}
