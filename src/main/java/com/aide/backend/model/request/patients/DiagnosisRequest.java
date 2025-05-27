package com.aide.backend.model.request.patients;

import lombok.Data;

@Data
public class DiagnosisRequest {
    private String diagnosisName;
    private String description;
    private String notes;
} 