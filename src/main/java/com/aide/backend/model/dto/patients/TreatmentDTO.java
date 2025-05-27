package com.aide.backend.model.dto.patients;

import lombok.Data;

@Data
public class TreatmentDTO {
    private Long id;
    private String description;
    private String notes;
}
