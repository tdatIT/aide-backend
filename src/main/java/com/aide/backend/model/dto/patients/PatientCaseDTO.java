package com.aide.backend.model.dto.patients;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PatientCaseDTO {
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String occupation;
    private String medicalHistory;
    private String dentalHistory;
    private String[] suggestedTests;
    private Long requestCounter;
    private List<ClinicalExamDTO> clinicalExams;
    private List<ParaclinicalTestDTO> paraclinicalTests;
    private DiagnosisDTO diagnosis;
    private TreatmentDTO treatment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
