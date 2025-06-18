package com.aide.backend.domain.dto.iam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoleRequest {
    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must be less than 50 characters")
    private String roleName;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
