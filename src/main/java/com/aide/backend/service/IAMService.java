package com.aide.backend.service;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.iam.CreateRoleRequest;
import com.aide.backend.domain.dto.iam.RoleDTO;
import com.aide.backend.domain.dto.iam.UserDTO;
import org.springframework.data.domain.Pageable;

public interface IAMService {
    PageResponse<UserDTO> listUsers(Pageable pageable);
    UserDTO toggleUserStatus(Long userId);
    PageResponse<RoleDTO> listRoles(Pageable pageable);
    RoleDTO createRole(CreateRoleRequest request);
    void deleteRole(Long roleId);
    UserDTO addRoleToUser(Long userId, Long roleId);
    UserDTO removeRoleFromUser(Long userId, Long roleId);
}
