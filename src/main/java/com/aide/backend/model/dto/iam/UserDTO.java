package com.aide.backend.model.dto.iam;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private boolean active;
    private String avatarUrl;
    private Set<RoleDTO> roles;
} 