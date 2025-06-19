package com.aide.backend.service.impl;

import com.aide.backend.common.PageResponse;
import com.aide.backend.domain.dto.patients.*;
import com.aide.backend.domain.entity.patients.*;
import com.aide.backend.domain.enums.PatientStatus;
import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.repository.*;
import com.aide.backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aide.backend.domain.enums.PatientStatus.UNPUBLISHED;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final ClinicalExamCategoryRepository clinicalExamCategoryRepository;
    private final ParaclinicalCateRepository paraclinicalTestCategoryRepository;
    private final ImageRepository imageRepository;
    private final ClinicalResultRepository cliResultRepository;
    private final ParaClinicalResultRepository paraCliResultRepository;

    @Override
    @Transactional
    public void create(CreatePatientRequest request) {
        var patient = Patient.builder()
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .requestCounter(0L)
                .occupation(request.getOccupation())
                .reasonForVisit(request.getReasonForVisit())
                .medicalHistory(request.getMedicalHistory())
                .dentalHistory(request.getDentalHistory())
                .suggestedTests(request.getSuggestedTests())
                .clinicalHistory(request.getClinicalHistory())
                .mode(request.getMode())
                .instruction(request.getInstruction())
                .status(UNPUBLISHED.toString())
                .build();

        handleClinicalExams(patient, request.getClinicalExams());
        handleParaclinicalTests(patient, request.getParaclinicalTests());
        handleDiagnosis(patient, request.getDiagnosis());
        handleTreatment(patient, request.getTreatment());
        repository.save(patient);
    }

    @Override
    @Transactional
    @CacheEvict(value = "patients", key = "#request.id")
    public void updatePatientInfo(UpdatePatientInfoRequest request) {
        var patient = repository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + request.getId()));

        patient.setName(request.getName());
        patient.setGender(request.getGender());
        patient.setAge(request.getAge());
        patient.setOccupation(request.getOccupation());
        patient.setReasonForVisit(request.getReasonForVisit());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setDentalHistory(request.getDentalHistory());
        patient.setClinicalHistory(request.getClinicalHistory());
        patient.setSuggestedTests(request.getSuggestedTests());
        patient.setMode(request.getMode());
        patient.setInstruction(request.getInstruction());
        patient.setStatus(UNPUBLISHED.toString());

        // Handle related entities updates
        handleUpdateDiagnosis(patient, request.getDiagnosis());
        handleUpdateTreatment(patient, request.getTreatment());
        repository.save(patient);
    }

    @Override
    @Transactional
    public void updatePatientTest(UpdatePatientTestRequest request) {
        var patient = repository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + request.getPatientId()));

        for (UpdateTestResultRequest clinicalResult : request.getClinicalTests()) {
            switch (clinicalResult.getAction()) {
                case CREATE -> {
                    var newClinicalExam = ClinicalResult.builder()
                            .patient(patient)
                            .clinicalCategories(findClinicalExamCategory(clinicalResult.getTestCategoryId()))
                            .textResult(clinicalResult.getTextResult())
                            .notes(clinicalResult.getNotes())
                            .build();
                    if (clinicalResult.getImageKeys() != null && clinicalResult.getImageKeys().length > 0) {
                        Set<Image> images = new HashSet<>();
                        for (Long imageKey : clinicalResult.getImageKeys()) {
                            if (isValidImageKey(imageKey)) {
                                images.add(findImage(imageKey));
                            }
                        }
                        newClinicalExam.setImages(images);
                    }
                    cliResultRepository.save(newClinicalExam);
                }
                case DELETE -> cliResultRepository.deleteById(clinicalResult.getId());
                case UPDATE -> {
                    ClinicalResult existingClinicalExam = cliResultRepository.findById(clinicalResult.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Clinical exam not found with id: " + clinicalResult.getId()));
                    existingClinicalExam.setTextResult(clinicalResult.getTextResult());
                    existingClinicalExam.setNotes(clinicalResult.getNotes());
                    existingClinicalExam.setClinicalCategories(findClinicalExamCategory(clinicalResult.getTestCategoryId()));

                    if (clinicalResult.getImageKeys() != null && clinicalResult.getImageKeys().length > 0) {
                        Set<Image> images = new HashSet<>();
                        for (Long imageKey : clinicalResult.getImageKeys()) {
                            if (isValidImageKey(imageKey)) {
                                images.add(findImage(imageKey));
                            }
                        }
                        existingClinicalExam.setImages(images);
                    } else {
                        existingClinicalExam.setImages(new HashSet<>());
                    }
                    cliResultRepository.save(existingClinicalExam);
                }
            }
        }

        for (UpdateTestResultRequest paraCliResult : request.getClinicalTests()) {
            switch (paraCliResult.getAction()) {
                case CREATE -> {
                    var newParaclinicalTest = ParaclinicalResult.builder()
                            .patient(patient)
                            .paraclinicalTestCategory(findParaclinicalTestCategory(paraCliResult.getTestCategoryId()))
                            .textResult(paraCliResult.getTextResult())
                            .notes(paraCliResult.getNotes())
                            .build();
                    if (paraCliResult.getImageKeys() != null && paraCliResult.getImageKeys().length > 0) {
                        Set<Image> images = new HashSet<>();
                        for (Long imageKey : paraCliResult.getImageKeys()) {
                            if (isValidImageKey(imageKey)) {
                                images.add(findImage(imageKey));
                            }
                        }
                        newParaclinicalTest.setImages(images);
                    }
                    paraCliResultRepository.save(newParaclinicalTest);
                }
                case DELETE -> paraCliResultRepository.deleteById(paraCliResult.getId());
                case UPDATE -> {
                    ParaclinicalResult existingParaClinicalTest = paraCliResultRepository.findById(paraCliResult.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test not found with id: " + paraCliResult.getId()));
                    existingParaClinicalTest.setTextResult(paraCliResult.getTextResult());
                    existingParaClinicalTest.setNotes(paraCliResult.getNotes());
                    existingParaClinicalTest.setParaclinicalTestCategory(findParaclinicalTestCategory(paraCliResult.getTestCategoryId()));

                    if (paraCliResult.getImageKeys() != null && paraCliResult.getImageKeys().length > 0) {
                        Set<Image> images = new HashSet<>();
                        for (Long imageKey : paraCliResult.getImageKeys()) {
                            if (isValidImageKey(imageKey)) {
                                images.add(findImage(imageKey));
                            }
                        }
                        existingParaClinicalTest.setImages(images);
                    } else {
                        existingParaClinicalTest.setImages(new HashSet<>());
                    }
                    paraCliResultRepository.save(existingParaClinicalTest);
                }
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "patients", key = "#id")
    public void softDeleteById(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));
        patient.setDeletedAt(LocalDateTime.now());
        repository.save(patient);
    }

    @Override
    @Cacheable(value = "patients", key = "#id")
    public PatientDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));
    }

    @Override
    public PageResponse<PatientDTO> findAll(Pageable pageable) {
        Page<Patient> page = repository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToDTO));
    }

    @Override
    @Transactional
    @CacheEvict(value = "patients", key = "#id")
    public void updateStatus(Long id, boolean isPublished) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient case not found with id: " + id));
        patient.setStatus(isPublished ? PatientStatus.PUBLISHED.toString() : PatientStatus.UNPUBLISHED.toString());
        repository.save(patient);
    }

    private PatientDTO mapToDTO(Patient patient) {
        return PatientDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .gender(patient.getGender().toString())
                .reasonForVisit(patient.getReasonForVisit())
                .age(patient.getAge())
                .status(patient.getStatus())
                .occupation(patient.getOccupation())
                .mode(patient.getMode().toString())
                .instruction(patient.getInstruction())
                .medicalHistory(patient.getMedicalHistory())
                .dentalHistory(patient.getDentalHistory())
                .suggestedTests(patient.getSuggestedTests())
                .clinicalHistory(patient.getClinicalHistory())
                .requestCounter(patient.getRequestCounter())
                .clinicalExResults(patient.getClinicalExResults().stream()
                        .map(this::mapToClinicalExamDTO)
                        .collect(Collectors.toList()))
                .paraclinicalExResults(patient.getParaclinicalExResults().stream()
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

    private ClinicalResultDTO mapToClinicalExamDTO(ClinicalResult result) {
        var dto = ClinicalResultDTO.builder()
                .id(result.getId())
                .clinicalCateId(result.getClinicalCategories().getId())
                .textResult(result.getTextResult())
                .testName(result.getClinicalCategories().getName())
                .notes(result.getNotes())
                .build();

        if (result.getImages() != null && !result.getImages().isEmpty()) {
            dto.setImages(result.getImages().stream()
                    .map(img ->
                            ImageDTO.builder().url(img.getUrl()).id(img.getId()).build())
                    .toArray(ImageDTO[]::new));
        }
        return dto;
    }

    private ParaclinicalResultDTO mapToParaclinicalTestDTO(ParaclinicalResult result) {
        var dto = ParaclinicalResultDTO.builder()
                .id(result.getId())
                .paraclinicalId(result.getParaclinicalTestCategory().getId())
                .textResult(result.getTextResult())
                .testName(result.getParaclinicalTestCategory().getName())
                .notes(result.getNotes())
                .build();
        if (result.getImages() != null && !result.getImages().isEmpty()) {
            dto.setImages(result.getImages().stream()
                    .map(img ->
                            ImageDTO.builder().url(img.getUrl()).id(img.getId()).build())
                    .toArray(ImageDTO[]::new));
        }
        return dto;
    }

    private DiagnosisDTO mapToDiagnosisDTO(Diagnosis diagnosis) {
        if (diagnosis == null) return null;
        return DiagnosisDTO.builder()
                .id(diagnosis.getId())
                .diagDiff(diagnosis.getDiagDiff())
                .diagPrelim(diagnosis.getDiagPrelim())
                .notes(diagnosis.getNotes())
                .build();
    }

    private TreatmentDTO mapToTreatmentDTO(Treatment treatment) {
        if (treatment == null) return null;
        return TreatmentDTO.builder()
                .id(treatment.getId())
                .treatmentNotes(treatment.getTreatmentNotes())
                .build();
    }

    private ClinicalCate findClinicalExamCategory(Long id) {
        return clinicalExamCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found with id: " + id));
    }

    private ParaclinicalCate findParaclinicalTestCategory(Long id) {
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

    private void handleClinicalExams(Patient patient, List<CreateTestResultRequest> clinicalExams) {
        if (clinicalExams == null || clinicalExams.isEmpty()) {
            return;
        }
        patient.setClinicalExResults(new HashSet<>(createClinicalExams(patient, clinicalExams)));
    }

    private void handleParaclinicalTests(Patient patient, List<CreateTestResultRequest> paraclinicalTests) {
        if (paraclinicalTests == null || paraclinicalTests.isEmpty()) {
            return;
        }
        patient.setParaclinicalExResults(new HashSet<>(createParaclinicalTests(patient, paraclinicalTests)));
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

    private List<ClinicalResult> createClinicalExams(Patient patient, List<CreateTestResultRequest> clinicalExams) {
        return clinicalExams.stream()
                .map(item -> {
                    ClinicalResult result = ClinicalResult.builder()
                            .patient(patient)
                            .clinicalCategories(findClinicalExamCategory(item.getTestCategoryId()))
                            .build();
                    result.setTextResult(item.getTextResult());
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

    private List<ParaclinicalResult> createParaclinicalTests(Patient patient, List<CreateTestResultRequest> paraclinicalTests) {
        return paraclinicalTests.stream()
                .map(item -> {
                    ParaclinicalResult result = ParaclinicalResult.builder()
                            .patient(patient)
                            .paraclinicalTestCategory(findParaclinicalTestCategory(item.getTestCategoryId()))
                            .build();
                    result.setNotes(item.getNotes());
                    result.setTextResult(item.getTextResult());

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

    private Diagnosis createDiagnosis(Patient patient, DiagnosisDTO diagnosisDTO) {
        return Diagnosis.builder()
                .patient(patient)
                .diagDiff(diagnosisDTO.getDiagDiff())
                .diagPrelim(diagnosisDTO.getDiagPrelim())
                .notes(diagnosisDTO.getNotes())
                .build();
    }

    private Treatment createTreatment(Patient patient, TreatmentDTO treatmentDTO) {
        return Treatment.builder()
                .patient(patient)
                .treatmentNotes(treatmentDTO.getTreatmentNotes())
                .build();
    }

    private void handleUpdateClinicalExams(Patient patient, List<UpdateTestResultRequest> clinicalExams) {
        if (clinicalExams == null || clinicalExams.isEmpty()) {
            patient.setClinicalExResults(null);
            return;
        }

        Set<ClinicalResult> updatedResults = new HashSet<>();
        for (UpdateTestResultRequest request : clinicalExams) {
            ClinicalResult result;
            if (request.getId() != null) {
                // Update existing result
                result = patient.getClinicalExResults().stream()
                        .filter(r -> r.getId().equals(request.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Clinical result not found with id: " + request.getId()));

                result.setTextResult(request.getTextResult());
                result.setNotes(request.getNotes());
                result.setClinicalCategories(findClinicalExamCategory(request.getTestCategoryId()));
            } else {
                // Create new result
                result = ClinicalResult.builder()
                        .patient(patient)
                        .clinicalCategories(findClinicalExamCategory(request.getTestCategoryId()))
                        .build();
                result.setTextResult(request.getTextResult());
                result.setNotes(request.getNotes());
            }

            // Handle images
            if (request.getImageKeys() != null && request.getImageKeys().length > 0) {
                Set<Image> images = new HashSet<>();
                for (Long imageKey : request.getImageKeys()) {
                    if (isValidImageKey(imageKey)) {
                        images.add(findImage(imageKey));
                    }
                }
                result.setImages(images);
            } else {
                result.setImages(new HashSet<>());
            }
            result.setPatient(patient);
            updatedResults.add(result);
        }

        patient.setClinicalExResults(updatedResults);
    }

    private void handleUpdateParaclinicalTests(Patient patient, List<UpdateTestResultRequest> paraclinicalTests) {
        if (paraclinicalTests == null || paraclinicalTests.isEmpty()) {
            // Clear existing paraclinical tests if none provided
            patient.setParaclinicalExResults(null);
            return;
        }

        Set<ParaclinicalResult> updatedResults = new HashSet<>();
        for (UpdateTestResultRequest request : paraclinicalTests) {
            ParaclinicalResult result;
            if (request.getId() != null) {
                // Update existing result
                result = patient.getParaclinicalExResults().stream()
                        .filter(r -> r.getId().equals(request.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Paraclinical result not found with id: " + request.getId()));

                result.setTextResult(request.getTextResult());
                result.setNotes(request.getNotes());
                result.setParaclinicalTestCategory(findParaclinicalTestCategory(request.getTestCategoryId()));
            } else {
                // Create new result
                result = ParaclinicalResult.builder()
                        .patient(patient)
                        .paraclinicalTestCategory(findParaclinicalTestCategory(request.getTestCategoryId()))
                        .build();
                result.setTextResult(request.getTextResult());
                result.setNotes(request.getNotes());
            }

            // Handle images
            if (request.getImageKeys() != null && request.getImageKeys().length > 0) {
                Set<Image> images = new HashSet<>();
                for (Long imageKey : request.getImageKeys()) {
                    if (isValidImageKey(imageKey)) {
                        images.add(findImage(imageKey));
                    }
                }
                result.setImages(images);
            } else {
                result.setImages(new HashSet<>());
            }

            updatedResults.add(result);
        }

        patient.setParaclinicalExResults(updatedResults);
    }

    private void handleUpdateDiagnosis(Patient patient, DiagnosisDTO diagnosis) {
        if (diagnosis == null) {
            return;
        }

        patient.getDiagnosis().setDiagDiff(diagnosis.getDiagDiff());
        patient.getDiagnosis().setDiagPrelim(diagnosis.getDiagPrelim());
        patient.getDiagnosis().setNotes(diagnosis.getNotes());
    }

    private void handleUpdateTreatment(Patient patient, TreatmentDTO treatment) {
        if (treatment == null) {
            return;
        }
        patient.getTreatment().setTreatmentNotes(treatment.getTreatmentNotes());
    }
}
