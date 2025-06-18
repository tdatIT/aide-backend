package com.aide.backend.service.impl;

import com.aide.backend.domain.dto.auth.LoginResponse;
import com.aide.backend.domain.dto.auth.UserProfileDTO;
import com.aide.backend.domain.entity.user.AuthUserDetails;
import com.aide.backend.domain.entity.user.User;
import com.aide.backend.domain.entity.user.UserCredential;
import com.aide.backend.domain.enums.CredentialType;
import com.aide.backend.domain.enums.RoleEnum;
import com.aide.backend.exception.BusinessException;
import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.repository.RoleRepository;
import com.aide.backend.repository.UserRepository;
import com.aide.backend.service.JwtService;
import com.aide.backend.service.OAuth2Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuth2ServiceImpl implements OAuth2Service {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Override
    @Transactional
    public LoginResponse handleGoogleIdToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                throw new BusinessException("Access token cannot be null or empty");
            }
            var transport = GoogleNetHttpTransport.newTrustedTransport();
            var jsonFactory = GsonFactory.getDefaultInstance();
            // Fetch user info from Google API
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(this.googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) {
                throw new RuntimeException("Invalid ID token.");
            }

            String email = (String) idToken.getPayload().get("email");
            String googleId = idToken.getPayload().getSubject();
            String name = (String) idToken.getPayload().get("name");
            String avatar = (String) idToken.getPayload().get("picture");

            // Create or get user
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> createGoogleUser(email, googleId, name, avatar));

            // Generate JWT tokens
            AuthUserDetails userDetails = new AuthUserDetails(
                    user.getId(),
                    user.getUsername(),
                    null, user.isActive(),
                    true,
                    true,
                    true,
                    user.getRoles());

            String jwtAccessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            return LoginResponse.builder().accessToken(jwtAccessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(24 * 60 * 60)
                    .profile(UserProfileDTO.builder().fullName(name).username(email).avatar(avatar).build())
                    .build();
        } catch (Exception e) {
            log.error("Error processing Google access token: {}", e.getMessage(), e);
            throw new BusinessException("Failed to process Google access token: " + e.getMessage());
        }
    }

    private User createGoogleUser(String email, String googleId, String name, String avatar) {
        var userRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER.toString()).
                orElseThrow(() -> new ResourceNotFoundException("User role not found"));

        var googleCred = UserCredential.builder()
                .credType(CredentialType.OIDC)
                .provider("GOOGLE")
                .oidcUserId(googleId)
                .active(true).build();

        var user = User.builder()
                .email(email)
                .username(email)
                .fullName(name)
                .avatarUrl(avatar)
                .active(true)
                .build();
        user.getCredentials().add(googleCred);
        user.getRoles().add(userRole);
        return userRepository.save(user);
    }
}
