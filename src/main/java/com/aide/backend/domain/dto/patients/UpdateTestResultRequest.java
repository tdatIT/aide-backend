package com.aide.backend.domain.dto.patients;

import com.aide.backend.domain.enums.Action;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTestResultRequest {
    private Action action;
    private Long id;
    private Long testCategoryId;
    private String textResult;
    private String notes;
    private Long[] imageKeys;
}


