package com.aide.backend.model.request.patients;

import lombok.Data;

@Data
public class ClinicalExamRequest {
    private Long testCategoryId;
    private String result;
    private String notes;
    private Long imageKey;
} 