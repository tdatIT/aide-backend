package com.aide.backend.model.dto.patients;

import lombok.Data;

@Data
public class DiagnosisDTO {
    private Long id;
    private String diagnosisName;
    private String description;
    private String notes;
}
