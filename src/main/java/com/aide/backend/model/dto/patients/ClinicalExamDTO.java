package com.aide.backend.model.dto.patients;

import lombok.Data;

@Data
public class ClinicalExamDTO {
    private Long id;
    private String name;
    private String notes;
    private String[] imageUrls;
}
