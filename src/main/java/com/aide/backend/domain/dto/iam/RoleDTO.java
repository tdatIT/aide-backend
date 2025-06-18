package com.aide.backend.domain.dto.iam;

import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String roleName;
    private String description;
    private boolean active;
}
