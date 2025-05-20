package com.aide.backend.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleTokenRequest {
    @NotBlank(message = "Google token is required")
    private String token;
}
