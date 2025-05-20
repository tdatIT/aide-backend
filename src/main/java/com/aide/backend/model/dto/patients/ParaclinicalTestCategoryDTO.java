package com.aide.backend.model.dto.patients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ParaclinicalTestCategoryDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must be less than 200 characters")
    private String name;
    
    private String description;
} 