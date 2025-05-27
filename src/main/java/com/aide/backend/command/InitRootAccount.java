package com.aide.backend.command;

import com.aide.backend.model.entity.user.Role;
import com.aide.backend.model.entity.user.User;
import com.aide.backend.model.enums.RoleEnum;
import com.aide.backend.repository.RoleRepository;
import com.aide.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitRootAccount {
    final UserRepository userRepository;
    final RoleRepository roleRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        if (userRepository.count() == 0) {
            var rootUser = new User();
            rootUser.setUsername("aide.teamvn@gmail.com");
            rootUser.setActive(true);
            rootUser.setEmail("aide.teamvn@gmail.com");
            rootUser.setFullName("AIDE Team VN");
            rootUser.setCreatedBy("system");

            rootUser.getRoles().add(
                    roleRepository.findByRoleName(RoleEnum.ROLE_SUPER_ADMIN.toString())
                            .orElseThrow(() -> new RuntimeException("Super Admin role not found")));

            userRepository.save(rootUser);
        }
    }
}
