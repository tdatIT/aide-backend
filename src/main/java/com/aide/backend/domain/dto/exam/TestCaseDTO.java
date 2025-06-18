package com.aide.backend.domain.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseDTO {
    private Long id;
    private String name;
    private String mode;
    private Long requestCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
