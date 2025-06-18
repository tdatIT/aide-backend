package com.aide.backend.service.impl;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.iam.CreateRoleRequest;
import com.aide.backend.domain.dto.iam.RoleDTO;
import com.aide.backend.domain.dto.iam.UserDTO;
import com.aide.backend.domain.entity.user.Role;
import com.aide.backend.domain.entity.user.User;
import com.aide.backend.domain.enums.RoleEnum;
import com.aide.backend.exception.BusinessException;
import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.repository.RoleRepository;
import com.aide.backend.repository.UserRepository;
import com.aide.backend.service.IAMService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IAMServiceImpl implements IAMService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public PageResponse<UserDTO> listUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToUserDTO));
    }

    @Override
    @Transactional
    public UserDTO toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setActive(!user.isActive());
        return mapToUserDTO(userRepository.save(user));
    }

    @Override
    public PageResponse<RoleDTO> listRoles(Pageable pageable) {
        Page<Role> page = roleRepository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToRoleDTO));
    }

    @Override
    @Transactional
    public RoleDTO createRole(CreateRoleRequest request) {
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new ResourceNotFoundException("Role with name " + request.getRoleName() + " already exists");
        }

        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setActive(true);

        return mapToRoleDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        if (role.getRoleName().equals(RoleEnum.ROLE_SUPER_ADMIN.name())) {
            throw new BusinessException("Cannot delete SUPER_ADMIN role");
        }

        role.setActive(false);
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public UserDTO addRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        if (role.getRoleName().equals(RoleEnum.ROLE_SUPER_ADMIN.name())) {
            // Check if there's already a super admin
            if (userRepository.existsByRoles_RoleName(RoleEnum.ROLE_SUPER_ADMIN.name())) {
                throw new BusinessException("A super admin already exists in the system");
            }
        }

        user.getRoles().add(role);
        return mapToUserDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        if (role.getRoleName().equals(RoleEnum.ROLE_SUPER_ADMIN.name())) {
            throw new BusinessException("Cannot remove SUPER_ADMIN role");
        }

        user.getRoles().remove(role);
        return mapToUserDTO(userRepository.save(user));
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setActive(user.isActive());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRoles(user.getRoles().stream().map(this::mapToRoleDTO).collect(java.util.stream.Collectors.toSet()));
        return dto;
    }

    private RoleDTO mapToRoleDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        dto.setActive(role.isActive());
        return dto;
    }
}
