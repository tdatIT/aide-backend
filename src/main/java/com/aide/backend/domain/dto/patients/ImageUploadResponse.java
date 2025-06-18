package com.aide.backend.domain.dto.patients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    private Long id;
    private String publicUrl;
}
