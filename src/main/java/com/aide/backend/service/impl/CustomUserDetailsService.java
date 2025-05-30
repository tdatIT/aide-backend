package com.aide.backend.service.impl;

import com.aide.backend.model.entity.user.AuthUserDetails;
import com.aide.backend.model.enums.CredentialType;
import com.aide.backend.repository.UserCredentialRepository;
import com.aide.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        var cred = userCredentialRepository.findByUsernameAndCredType(username, CredentialType.PASSWORD);

        String password = "";
        if (cred.isPresent()) {
            password = cred.get().getPassword();
        }

        return new AuthUserDetails(
                user.getUsername(),
                password,
                user.isActive(),
                true,
                true,
                true,
                user.getRoles());
    }
}
