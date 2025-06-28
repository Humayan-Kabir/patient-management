package com.dev.healthcare.patient_service.service;

import com.dev.healthcare.patient_service.dto.PatientResponseDTO;
import com.dev.healthcare.patient_service.mapper.PatientMapper;
import com.dev.healthcare.patient_service.repository.PatientRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toDTO).toList();
    }
}
