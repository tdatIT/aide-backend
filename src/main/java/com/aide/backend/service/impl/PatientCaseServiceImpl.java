package com.aide.backend.service.impl;

import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.model.dto.patients.*;
import com.aide.backend.model.dto.common.PageResponse;
import com.aide.backend.model.entity.patients.*;
import com.aide.backend.model.enums.Gender;
import com.aide.backend.repository.*;
import com.aide.backend.service.PatientCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientCaseServiceImpl implements PatientCaseService {

    private final PatientCaseRepository repository;
    private final ClinicalExamCategoryRepository clinicalExamCategoryRepository;
    private final ParaclinicalTestCategoryRepository paraclinicalTestCategoryRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public PatientCaseDTO create(CreatePatientCaseRequest request) {
        Patient patient = new Patient();
        updatePatientFromRequest(patient, request);
        patient.setStatus(1);
        return mapToDTO(repository.save(patient));
    }

    @Override
    @Transactional
    public PatientCaseDTO update(Long id, CreatePatientCaseRequest request) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));

        updatePatientFromRequest(patient, request);
        return mapToDTO(repository.save(patient));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));
        patient.setDeletedAt(LocalDateTime.now());
        repository.save(patient);
    }

    @Override
    public PatientCaseDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));
    }

    @Override
    public PageResponse<PatientCaseDTO> findAll(Pageable pageable) {
        Page<Patient> page = repository.findByStatus(1, pageable);
        return PageResponse.of(page.map(this::mapToDTO));
    }

    private void updatePatientFromRequest(Patient patient, CreatePatientCaseRequest request) {
        patient.setName(request.getName());
        patient.setGender(request.getGender());
        patient.setAge(request.getAge());
        patient.setOccupation(request.getOccupation());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setDentalHistory(request.getDentalHistory());
        patient.setSuggestedTests(request.getSuggestedTests());
        patient.setRequestCounter(request.getRequestCounter());

        // Update clinical exams
        if (request.getClinicalExams() != null) {
            List<ClinicalExamResult> clinicalExams = request.getClinicalExams().stream()
                    .map(item -> {
                        ClinicalExamResult result = new ClinicalExamResult();
                        result.setPatient(patient);
                        result.setClinicalExamCategories(clinicalExamCategoryRepository.findById(item.getTestCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found")));
                        result.setResult(item.getResult());
                        result.setNotes(item.getNotes());
                        if (item.getImageKey() != null) {
                            result.setImage(imageRepository.findById(item.getImageKey())
                                    .orElseThrow(() -> new ResourceNotFoundException("Image not found")));
                        }
                        return result;
                    })
                    .toList();
            patient.setClinicalExamResults(new HashSet<>(clinicalExams));
        }

        // Update paraclinical tests
        if (request.getParaclinicalTests() != null) {
            List<ParaclinicalExamResult> paraclinicalTests = request.getParaclinicalTests().stream()
                    .map(item -> {
                        ParaclinicalExamResult result = new ParaclinicalExamResult();
                        result.setPatient(patient);
                        result.setParaclinicalTestCategory(paraclinicalTestCategoryRepository.findById(item.getTestCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test category not found")));
                        result.setResult(item.getResult());
                        result.setNotes(item.getNotes());
                        if (item.getImageKey() != null) {
                            result.setImage(imageRepository.findById(item.getImageKey())
                                    .orElseThrow(() -> new ResourceNotFoundException("Image not found")));
                        }
                        return result;
                    })
                    .toList();
            patient.setParaclinicalExamResults(new HashSet<>(paraclinicalTests));
        }

        // Update diagnosis
        if (request.getDiagnosis() != null) {
            Diagnosis diagnosis = patient.getDiagnosis();
            if (diagnosis == null) {
                diagnosis = new Diagnosis();
                diagnosis.setPatient(patient);
            }
            diagnosis.setDiagnosisName(request.getDiagnosis().getDiagnosisName());
            diagnosis.setDescription(request.getDiagnosis().getDescription());
            diagnosis.setNotes(request.getDiagnosis().getNotes());
            patient.setDiagnosis(diagnosis);
        }

        // Update treatment
        if (request.getTreatment() != null) {
            Treatment treatment = patient.getTreatment();
            if (treatment == null) {
                treatment = new Treatment();
                treatment.setPatient(patient);
            }
            treatment.setDescription(request.getTreatment().getDescription());
            treatment.setNotes(request.getTreatment().getNotes());
            patient.setTreatment(treatment);
        }
    }

    private PatientCaseDTO mapToDTO(Patient patient) {
        return PatientCaseDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .gender(patient.getGender().toString())
                .age(patient.getAge())
                .occupation(patient.getOccupation())
                .medicalHistory(patient.getMedicalHistory())
                .dentalHistory(patient.getDentalHistory())
                .suggestedTests(patient.getSuggestedTests())
                .requestCounter(patient.getRequestCounter())
                .clinicalExams(patient.getClinicalExamResults().stream()
                        .map(this::mapToClinicalExamDTO)
                        .collect(Collectors.toList()))
                .paraclinicalTests(patient.getParaclinicalExamResults().stream()
                        .map(this::mapToParaclinicalTestDTO)
                        .collect(Collectors.toList()))
                .diagnosis(mapToDiagnosisDTO(patient.getDiagnosis()))
                .treatment(mapToTreatmentDTO(patient.getTreatment()))
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .createdBy(patient.getCreatedBy())
                .updatedBy(patient.getUpdatedBy())
                .build();
    }

    private ClinicalExamDTO mapToClinicalExamDTO(ClinicalExamResult result) {
        ClinicalExamDTO dto = new ClinicalExamDTO();
        dto.setId(result.getId());
        dto.setName(result.getClinicalExamCategories().getName());
        dto.setNotes(result.getNotes());
        dto.setResult(result.getResult());
        if (result.getImage() != null) {
            dto.setImageUrl(result.getImage().getUrl());
        }
        return dto;
    }

    private ParaclinicalTestDTO mapToParaclinicalTestDTO(ParaclinicalExamResult result) {
        ParaclinicalTestDTO dto = new ParaclinicalTestDTO();
        dto.setId(result.getId());
        dto.setName(result.getParaclinicalTestCategory().getName());
        dto.setNotes(result.getNotes());
        dto.setResult(result.getResult());
        if (result.getImage() != null) {
            dto.setImageUrl(result.getImage().getUrl());
        }
        return dto;
    }

    private DiagnosisDTO mapToDiagnosisDTO(Diagnosis diagnosis) {
        if (diagnosis == null) return null;
        DiagnosisDTO dto = new DiagnosisDTO();
        dto.setId(diagnosis.getId());
        dto.setDiagnosisName(diagnosis.getDiagnosisName());
        dto.setDescription(diagnosis.getDescription());
        dto.setNotes(diagnosis.getNotes());
        return dto;
    }

    private TreatmentDTO mapToTreatmentDTO(Treatment treatment) {
        if (treatment == null) return null;
        TreatmentDTO dto = new TreatmentDTO();
        dto.setDescription(treatment.getDescription());
        dto.setNotes(treatment.getNotes());
        return dto;
    }
}
