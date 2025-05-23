package com.aide.backend.model.dto.patients;

import lombok.Data;

@Data
public class ParaclinicalTestDTO {
    private Long id;
    private String name;
    private String notes;
    private String result;
    private String imageUrl;
}
