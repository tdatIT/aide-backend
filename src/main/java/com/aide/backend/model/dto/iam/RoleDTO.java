package com.aide.backend.model.dto.iam;

import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String roleName;
    private String description;
    private boolean active;
} 