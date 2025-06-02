package com.aide.backend.service.impl;

import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.model.dto.patients.*;
import com.aide.backend.model.dto.common.PageResponse;
import com.aide.backend.model.entity.patients.*;
import com.aide.backend.model.enums.PatientStatus;
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
import java.util.Set;
import java.util.stream.Collectors;

import static com.aide.backend.model.enums.PatientStatus.UNPUBLISHED;

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
        patient.setStatus(UNPUBLISHED.toString());
        patient.setGender(request.getGender());
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setReasonForVisit(request.getReasonForVisit());
        patient.setOccupation(request.getOccupation());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setDentalHistory(request.getDentalHistory());
        patient.setSuggestedTests(request.getSuggestedTests());

        handleClinicalExams(patient, request.getClinicalExams());
        handleParaclinicalTests(patient, request.getParaclinicalTests());
        handleDiagnosis(patient, request.getDiagnosis());
        handleTreatment(patient, request.getTreatment());

        return mapToDTO(repository.save(patient));
    }

    @Override
    @Transactional
    public PatientCaseDTO update(Long id, CreatePatientCaseRequest request) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));

        updateBasicPatientInfo(patient, request);

        // Update clinical exams
        if (request.getClinicalExams() != null) {
            patient.setClinicalExamResults(new HashSet<>(updateClinicalExams(patient, request.getClinicalExams())));
        }

        // Update paraclinical tests
        if (request.getParaclinicalTests() != null) {
            patient.setParaclinicalExamResults(new HashSet<>(updateParaclinicalTests(patient, request.getParaclinicalTests())));
        }

        // Update diagnosis
        if (request.getDiagnosis() != null) {
            patient.setDiagnosis(updateDiagnosis(patient, request.getDiagnosis()));
        }

        // Update treatment
        if (request.getTreatment() != null) {
            patient.setTreatment(updateTreatment(patient, request.getTreatment()));
        }

        return mapToDTO(repository.save(patient));
    }

    private void updateBasicPatientInfo(Patient patient, CreatePatientCaseRequest request) {
        patient.setName(request.getName());
        patient.setGender(request.getGender());
        patient.setAge(request.getAge());
        patient.setReasonForVisit(request.getReasonForVisit());
        patient.setOccupation(request.getOccupation());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setDentalHistory(request.getDentalHistory());
        patient.setSuggestedTests(request.getSuggestedTests());
    }

    private void handleClinicalExams(Patient patient, List<CreateTestResultItems> clinicalExams) {
        if (clinicalExams == null || clinicalExams.isEmpty()) {
            return;
        }
        patient.setClinicalExamResults(new HashSet<>(createClinicalExams(patient, clinicalExams)));
    }

    private void handleParaclinicalTests(Patient patient, List<CreateTestResultItems> paraclinicalTests) {
        if (paraclinicalTests == null || paraclinicalTests.isEmpty()) {
            return;
        }
        patient.setParaclinicalExamResults(new HashSet<>(createParaclinicalTests(patient, paraclinicalTests)));
    }

    private void handleDiagnosis(Patient patient, DiagnosisDTO diagnosis) {
        if (diagnosis == null) {
            return;
        }
        patient.setDiagnosis(createDiagnosis(patient, diagnosis));
    }

    private void handleTreatment(Patient patient, TreatmentDTO treatment) {
        if (treatment == null) {
            return;
        }
        patient.setTreatment(createTreatment(patient, treatment));
    }

    private List<ClinicalExamResult> createClinicalExams(Patient patient, List<CreateTestResultItems> clinicalExams) {
        return clinicalExams.stream()
                .map(item -> {
                    ClinicalExamResult result = ClinicalExamResult.builder()
                            .patient(patient)
                            .clinicalExamCategories(findClinicalExamCategory(item.getTestCategoryId()))
                            .build();
                    result.setNotes(item.getNotes());

                    if (item.getImageKeys() != null && item.getImageKeys().length > 0) {
                        Set<Image> images = new HashSet<>();
                        for (Long imageKey : item.getImageKeys()) {
                            if (isValidImageKey(imageKey)) {
                                images.add(findImage(imageKey));
                            }
                        }
                        result.setImages(images);
                    }
                    return result;
                })
                .toList();
    }

    private List<ClinicalExamResult> updateClinicalExams(Patient patient, List<CreateTestResultItems> clinicalExams) {
        return createClinicalExams(patient, clinicalExams);
    }

    private List<ParaclinicalExamResult> createParaclinicalTests(Patient patient, List<CreateTestResultItems> paraclinicalTests) {
        return paraclinicalTests.stream()
                .map(item -> {
                    ParaclinicalExamResult result = ParaclinicalExamResult.builder()
                            .patient(patient)
                            .paraclinicalTestCategory(findParaclinicalTestCategory(item.getTestCategoryId()))
                            .build();
                    result.setNotes(item.getNotes());

                    if (item.getImageKeys() != null && item.getImageKeys().length > 0) {
                        Set<Image> images = new HashSet<>();
                        for (Long imageKey : item.getImageKeys()) {
                            if (isValidImageKey(imageKey)) {
                                images.add(findImage(imageKey));
                            }
                        }
                        result.setImages(images);
                    }
                    return result;
                })
                .toList();
    }

    private List<ParaclinicalExamResult> updateParaclinicalTests(Patient patient, List<CreateTestResultItems> paraclinicalTests) {
        return createParaclinicalTests(patient, paraclinicalTests);
    }

    private Diagnosis createDiagnosis(Patient patient, DiagnosisDTO diagnosisDTO) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatient(patient);
        diagnosis.setDiagnosisName(diagnosisDTO.getDiagnosisName());
        diagnosis.setDescription(diagnosisDTO.getDescription());
        return diagnosis;
    }

    private Diagnosis updateDiagnosis(Patient patient, DiagnosisDTO diagnosisDTO) {
        Diagnosis diagnosis = patient.getDiagnosis();
        if (diagnosis == null) {
            diagnosis = new Diagnosis();
            diagnosis.setPatient(patient);
        }
        diagnosis.setDiagnosisName(diagnosisDTO.getDiagnosisName());
        diagnosis.setDescription(diagnosisDTO.getDescription());
        return diagnosis;
    }

    private Treatment createTreatment(Patient patient, TreatmentDTO treatmentDTO) {
        Treatment treatment = new Treatment();
        treatment.setPatient(patient);
        treatment.setDescription(treatmentDTO.getDescription());
        return treatment;
    }

    private Treatment updateTreatment(Patient patient, TreatmentDTO treatmentDTO) {
        Treatment treatment = patient.getTreatment();
        if (treatment == null) {
            treatment = new Treatment();
            treatment.setPatient(patient);
        }
        treatment.setDescription(treatmentDTO.getDescription());
        return treatment;
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
        Page<Patient> page = repository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToDTO));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, boolean isPublished) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));
        patient.setStatus(isPublished ? PatientStatus.PUBLISHED.toString() : PatientStatus.UNPUBLISHED.toString());
        repository.save(patient);
    }

    private PatientCaseDTO mapToDTO(Patient patient) {
        return PatientCaseDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .gender(patient.getGender().toString())
                .age(patient.getAge())
                .status(patient.getStatus())
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
        if (result.getImages() != null && !result.getImages().isEmpty()) {
            dto.setImageUrls(result.getImages().stream()
                    .map(Image::getUrl)
                    .toArray(String[]::new));
        }
        return dto;
    }

    private ParaclinicalTestDTO mapToParaclinicalTestDTO(ParaclinicalExamResult result) {
        ParaclinicalTestDTO dto = new ParaclinicalTestDTO();
        dto.setId(result.getId());
        dto.setName(result.getParaclinicalTestCategory().getName());
        dto.setNotes(result.getNotes());
        if (result.getImages() != null && !result.getImages().isEmpty()) {
            dto.setImageUrls(result.getImages().stream()
                    .map(Image::getUrl)
                    .toArray(String[]::new));
        }
        return dto;
    }

    private DiagnosisDTO mapToDiagnosisDTO(Diagnosis diagnosis) {
        if (diagnosis == null) return null;
        DiagnosisDTO dto = new DiagnosisDTO();
        dto.setId(diagnosis.getId());
        dto.setDiagnosisName(diagnosis.getDiagnosisName());
        dto.setDescription(diagnosis.getDescription());
        return dto;
    }

    private TreatmentDTO mapToTreatmentDTO(Treatment treatment) {
        if (treatment == null) return null;
        TreatmentDTO dto = new TreatmentDTO();
        dto.setId(treatment.getId());
        dto.setDescription(treatment.getDescription());
        return dto;
    }

    private ClinicalExamCategory findClinicalExamCategory(Long id) {
        return clinicalExamCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found with id: " + id));
    }

    private ParaclinicalTestCategory findParaclinicalTestCategory(Long id) {
        return paraclinicalTestCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test category not found with id: " + id));
    }

    private Image findImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
    }

    private boolean isValidImageKey(Long imageKey) {
        return imageKey != null && imageKey != 0;
    }
}
