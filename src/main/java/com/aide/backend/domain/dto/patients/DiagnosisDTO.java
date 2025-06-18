package com.aide.backend.domain.dto.patients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDTO {
    private Long id;
    private String diagPrelim;
    private String diagDiff;
    private String notes;
}
