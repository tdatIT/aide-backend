package com.aide.backend.domain.dto.patients;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTestResultRequest {
    private Long testCategoryId;
    private String textResult;
    private String notes;
    private Long[] imageKeys;
}


