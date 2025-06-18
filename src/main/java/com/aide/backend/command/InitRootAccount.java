package com.aide.backend.command;

import com.aide.backend.domain.entity.user.User;
import com.aide.backend.domain.entity.user.UserCredential;
import com.aide.backend.domain.enums.CredentialType;
import com.aide.backend.domain.enums.RoleEnum;
import com.aide.backend.repository.RoleRepository;
import com.aide.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

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

            var credential = new UserCredential();
            credential.setCredType(CredentialType.PASSWORD);
            credential.setActive(true);
            credential.setPassword("TEMP");
            credential.setUser(rootUser);


            rootUser.setCredentials(Set.of(credential));
            rootUser.setRoles(
                    Set.of(roleRepository.findByRoleName(RoleEnum.ROLE_SUPER_ADMIN.toString())
                            .orElseThrow(() -> new RuntimeException("Super Admin role not found"))));

            userRepository.save(rootUser);
        }
    }
}
