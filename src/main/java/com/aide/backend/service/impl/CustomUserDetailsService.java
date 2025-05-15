package com.aide.backend.service.impl;

import com.aide.backend.model.entity.AuthUserDetails;
import com.aide.backend.model.enums.CredentialType;
import com.aide.backend.repository.UserCredentialRepository;
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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByUsernameAndCredType(username, CredentialType.PASSWORD)
                .map(credential -> new AuthUserDetails(
                        credential.getUser().getUsername(),
                        credential.getPassword(),
                        credential.isActive(),
                        true,
                        true,
                        true
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
} 