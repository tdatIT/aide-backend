package com.aide.backend.service.impl;

import com.aide.backend.exception.BusinessException;
import com.aide.backend.model.dto.auth.TokenResponse;
import com.aide.backend.model.entity.AuthUserDetails;
import com.aide.backend.model.entity.User;
import com.aide.backend.model.entity.UserCredential;
import com.aide.backend.model.enums.CredentialType;
import com.aide.backend.repository.UserCredentialRepository;
import com.aide.backend.repository.UserRepository;
import com.aide.backend.service.OAuth2Service;
import com.aide.backend.service.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtService jwtService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Override
    @Transactional
    public TokenResponse verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new BusinessException("Invalid ID token");
            }

            Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();

            UserCredential userCred = userCredentialRepository.findByUsernameAndCredType(email, CredentialType.OIDC)
                    .orElseGet(() -> createGoogleUser(email, googleId, payload));

            AuthUserDetails userDetails = new AuthUserDetails(
                    userCred.getUser().getUsername(),
                    userCred.getPassword(),
                    userCred.isActive(),
                    true,
                    true,
                    true
            );

            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(24 * 60 * 60) // 24 hours in seconds
                    .build();

        } catch (Exception e) {
            throw new BusinessException("Failed to verify Google token", e);
        }
    }

    private UserCredential createGoogleUser(String email, String googleId, Payload payload) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        user.setFullName((String) payload.get("name"));
        user.setActive(true);
        userRepository.save(user);

        UserCredential googleCred = new UserCredential();
        googleCred.setUser(user);
        googleCred.setCredType(CredentialType.OIDC);
        googleCred.setProvider("GOOGLE");
        googleCred.setOidcUserId(googleId);
        googleCred.setActive(true);
        return userCredentialRepository.save(googleCred);
    }
} 