package com.aide.backend.domain.dto.patients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParaclinicalResultDTO {
    private Long id;
    private String testName;
    private String textResult;
    private String notes;
    private String[] imageUrls;
}
