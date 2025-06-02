package com.aide.backend.model.dto.patients;

import com.aide.backend.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreatePatientCaseRequest {
    @NotBlank
    private String name;

    @NotBlank
    private Gender gender;

    @NotEmpty
    private Integer age;

    private String occupation;

    @NotBlank
    private String reasonForVisit;

    @NotBlank
    private String medicalHistory;

    @NotBlank
    private String dentalHistory;

    @NotEmpty
    private String[] suggestedTests;

    @NotEmpty
    private List<CreateTestResultItems> clinicalExams;

    @NotEmpty
    private List<CreateTestResultItems> paraclinicalTests;

    @NotNull
    private DiagnosisDTO diagnosis;

    @NotNull
    private TreatmentDTO treatment;
}
