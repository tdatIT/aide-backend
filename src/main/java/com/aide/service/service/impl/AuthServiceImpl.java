package com.aide.service.service.impl;

import com.aide.service.exception.BusinessException;
import com.aide.service.model.dto.auth.LoginRequest;
import com.aide.service.model.dto.auth.RegisterRequest;
import com.aide.service.model.dto.auth.TokenResponse;
import com.aide.service.model.entity.Role;
import com.aide.service.model.entity.User;
import com.aide.service.model.entity.UserCredential;
import com.aide.service.model.enums.CredentialType;
import com.aide.service.repository.RoleRepository;
import com.aide.service.repository.UserCredentialRepository;
import com.aide.service.repository.UserRepository;
import com.aide.service.service.AuthService;
import com.aide.service.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserCredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void register(RegisterRequest request) {
        if (credentialRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        if (credentialRepository.existsByUsername(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new BusinessException("Default role not found"));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setActive(true);
        user.setRoles(new HashSet<>(Set.of(userRole)));
        userRepository.save(user);

        // Create password credential
        UserCredential passwordCred = new UserCredential();
        passwordCred.setUser(user);
        passwordCred.setCredType(CredentialType.PASSWORD);
        passwordCred.setUsername(request.getUsername());
        passwordCred.setSecret(passwordEncoder.encode(request.getPassword()));
        passwordCred.setActive(true);
        credentialRepository.save(passwordCred);

        // Create email credential
        UserCredential emailCred = new UserCredential();
        emailCred.setUser(user);
        emailCred.setCredType(CredentialType.PASSWORD);
        emailCred.setUsername(request.getEmail());
        emailCred.setSecret(passwordEncoder.encode(request.getPassword()));
        emailCred.setActive(true);
        credentialRepository.save(emailCred);
    }

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(24 * 60 * 60) // 24 hours in seconds
                .build();
    }

    public void logout(String token) {
        String username = jwtService.extractUsername(token);
        String key = "blacklist:" + token;
        redisTemplate.opsForValue().set(key, username);
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
    }

    public TokenResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = credentialRepository.findByUsername(username)
                .map(cred -> org.springframework.security.core.userdetails.User.builder()
                        .username(cred.getUsername())
                        .password(cred.getSecret())
                        .authorities(cred.getUser().getRoles().stream()
                                .map(role -> "ROLE_" + role.getRoleName())
                                .toArray(String[]::new))
                        .build())
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new BusinessException("Invalid refresh token");
        }

        String accessToken = jwtService.generateToken(userDetails);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(24 * 60 * 60)
                .build();
    }
} 