package com.aide.backend.model.dto.patients;

import lombok.Data;

@Data
public class ParaclinicalTestDTO {
    private Long id;
    private String testName;
    private String textResult;
    private String notes;
    private String[] imageUrls;
}
