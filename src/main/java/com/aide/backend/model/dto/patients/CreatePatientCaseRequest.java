package com.aide.backend.model.dto.patients;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientCaseRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    private String gender;

    @NotBlank
    private Integer age;

    @NotBlank
    private String reason;

    @NotBlank
    private String generalHistory;

    @NotBlank
    private String dentalHistory;

    @NotBlank
    private String diseaseHistory;

    @NotBlank
    private String generalExam;

    @NotBlank
    private String facialExam;

    @NotBlank
    private String oralExam;

    private String additionalNotes;

    @NotEmpty
    private TestResultItemRequest[] testResults;



}

