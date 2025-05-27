package com.aide.backend.controller.admin;

import com.aide.backend.model.dto.iam.CreateRoleRequest;
import com.aide.backend.model.dto.iam.RoleDTO;
import com.aide.backend.model.dto.iam.UserDTO;
import com.aide.backend.model.entity.user.Role;
import com.aide.backend.model.entity.user.User;
import com.aide.backend.model.enums.RoleEnum;
import com.aide.backend.service.IAMService;
import com.aide.backend.model.dto.common.PageResponse;
import com.aide.backend.model.dto.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/iam")
@RequiredArgsConstructor
public class IAMController {

    private final IAMService iamService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<PageResponse<UserDTO>>> listUsers(Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(iamService.listUsers(pageable)));
    }

    @PutMapping("/users/{userId}/toggle-status")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<UserDTO>> toggleUserStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(BaseResponse.success(iamService.toggleUserStatus(userId)));
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<PageResponse<RoleDTO>>> listRoles(Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(iamService.listRoles(pageable)));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<RoleDTO>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.ok(BaseResponse.success(iamService.createRole(request)));
    }

    @DeleteMapping("/roles/{roleId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<Void>> deleteRole(@PathVariable Long roleId) {
        iamService.deleteRole(roleId);
        return ResponseEntity.ok(BaseResponse.success("Role deleted successfully", null));
    }

    @PostMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<UserDTO>> addRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(BaseResponse.success(iamService.addRoleToUser(userId, roleId)));
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<BaseResponse<UserDTO>> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(BaseResponse.success(iamService.removeRoleFromUser(userId, roleId)));
    }
} 