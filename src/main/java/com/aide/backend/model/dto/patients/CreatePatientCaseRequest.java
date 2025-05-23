package com.aide.backend.model.dto.patients;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreatePatientCaseRequest {
    private String name;
    private String gender;
    private Integer age;
    private String occupation;
    private String medicalHistory;
    private String dentalHistory;
    private String[] suggestedTests;
    private Long requestCounter;
    private List<CreateTestResultItems> clinicalExams;
    private List<CreateTestResultItems> paraclinicalTests;
    private DiagnosisDTO diagnosis;
    private TreatmentDTO treatment;
}
