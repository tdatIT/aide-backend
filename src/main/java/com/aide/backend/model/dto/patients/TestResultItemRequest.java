package com.aide.backend.model.dto.patients;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultItemRequest {
    @NotBlank
    private String testType;

    private String resultText;

    private Long imgKey;
}
