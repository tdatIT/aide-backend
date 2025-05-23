package com.aide.backend.model.dto.patients;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTestResultItems {
    private Long testCategoryId;
    private String result;
    private String notes;
    private Long imageKey;
}


