package com.aide.backend.domain.dto.patients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String occupation;
    private String reasonForVisit;
    private String clinicalHistory;
    private String medicalHistory;
    private String dentalHistory;
    private String[] suggestedTests;
    private Long requestCounter;
    private String status;
    private String mode;
    private String instruction;
    private List<ClinicalResultDTO> clinicalExResults;
    private List<ParaclinicalResultDTO> paraclinicalExResults;
    private DiagnosisDTO diagnosis;
    private TreatmentDTO treatment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
