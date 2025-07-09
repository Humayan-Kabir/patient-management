package com.dev.healthcare.patient_service.service;

import com.dev.healthcare.patient_service.dto.PatientRequestDTO;
import com.dev.healthcare.patient_service.dto.PatientResponseDTO;
import com.dev.healthcare.patient_service.exception.EmailAlreadyExistException;
import com.dev.healthcare.patient_service.exception.PatientNotFoundException;
import com.dev.healthcare.patient_service.grpc.BillingServiceGrpcClient;
import com.dev.healthcare.patient_service.mapper.PatientMapper;
import com.dev.healthcare.patient_service.model.Patient;
import com.dev.healthcare.patient_service.repository.PatientRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository,
        BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException(
                "A patient with this email already exist: " + patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),
            patient.getEmail(), patient.getName());

        return PatientMapper.toDTO(patient);
    }

    public List<PatientResponseDTO> getPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
            () -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistException(
                "Patient already exists with email: " + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
