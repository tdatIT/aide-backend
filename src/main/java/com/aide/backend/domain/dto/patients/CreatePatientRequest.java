package com.aide.backend.domain.dto.patients;

import com.aide.backend.domain.enums.Gender;
import com.aide.backend.domain.enums.Mode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreatePatientRequest {
    @NotBlank
    private String name;

    @NotBlank
    private Gender gender;

    @NotEmpty
    private Integer age;

    private String occupation;

    @NotEmpty
    private Mode mode;

    @NotBlank
    private String reasonForVisit;

    @NotBlank
    private String medicalHistory;

    @NotBlank
    private String dentalHistory;

    private String clinicalHistory;

    @NotEmpty
    private String[] suggestedTests;

    private String instruction;

    @NotEmpty
    private List<CreateTestResultRequest> clinicalExams;

    @NotEmpty
    private List<CreateTestResultRequest> paraclinicalTests;

    @NotNull
    private DiagnosisDTO diagnosis;

    @NotNull
    private TreatmentDTO treatment;
}
