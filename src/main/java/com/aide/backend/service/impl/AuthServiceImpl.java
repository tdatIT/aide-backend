package com.aide.backend.service.impl;

import com.aide.backend.domain.dto.auth.*;
import com.aide.backend.domain.entity.user.Role;
import com.aide.backend.domain.entity.user.User;
import com.aide.backend.domain.entity.user.UserCredential;
import com.aide.backend.domain.enums.CredentialType;
import com.aide.backend.domain.enums.RoleEnum;
import com.aide.backend.exception.BusinessException;
import com.aide.backend.repository.RoleRepository;
import com.aide.backend.repository.UserCredentialRepository;
import com.aide.backend.repository.UserRepository;
import com.aide.backend.service.AuthService;
import com.aide.backend.service.JwtService;
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

import static com.aide.backend.domain.constant.Constants.MAX_ACCESS_TOKEN_EXP;

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
        if (userRepository.existsByUsername(request.getEmail())) {
            throw new BusinessException("Username already exists");
        }

        Role userRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER.toString())
                .orElseThrow(() -> new BusinessException("Default role not found"));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setActive(true);
        user.setRoles(new HashSet<>(Set.of(userRole)));


        // Create password credential
        UserCredential passwordCred = new UserCredential();
        passwordCred.setCredType(CredentialType.PASSWORD);
        passwordCred.setCredType(CredentialType.PASSWORD);
        passwordCred.setPassword(passwordEncoder.encode(request.getPassword()));
        passwordCred.setActive(true);
        passwordCred.setUser(user);
        user.setCredentials(Set.of(passwordCred));

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("User not found"));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(MAX_ACCESS_TOKEN_EXP)
                .profile(
                        UserProfileDTO.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .roles(user.getRoles().stream().map(Role::getRoleName).distinct().toArray(String[]::new))
                                .build()
                )
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

        UserDetails userDetails = credentialRepository.findByUsernameAndCredType(username, CredentialType.PASSWORD)
                .map(cred -> org.springframework.security.core.userdetails.User.builder()
                        .username(cred.getUser().getUsername())
                        .password(cred.getPassword())
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
                .expiresIn(24 * 60 * 60)
                .build();
    }
}
