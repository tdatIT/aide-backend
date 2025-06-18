package com.aide.backend.domain.dto.patients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ParaclinicalCateDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must be less than 200 characters")
    private String name;

    private String description;
}
