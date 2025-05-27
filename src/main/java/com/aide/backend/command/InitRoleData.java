package com.aide.backend.command;

import com.aide.backend.model.entity.user.Role;
import com.aide.backend.model.enums.RoleEnum;
import com.aide.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitRoleData {
    final RoleRepository roleRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event){
        if (roleRepository.count() == 0) {
            var userRole = new Role();
            userRole.setRoleName(RoleEnum.ROLE_USER.name());

            var adminRole = new Role();
            adminRole.setRoleName(RoleEnum.ROLE_ADMIN.name());

            var superAdminRole = new Role();
            superAdminRole.setRoleName(RoleEnum.ROLE_SUPER_ADMIN.name());

            roleRepository.save(userRole);
            roleRepository.save(adminRole);
            roleRepository.save(superAdminRole);
        }
    }
}
