package com.aide.backend.service.impl;

import com.aide.backend.exception.BusinessException;
import com.aide.backend.model.dto.auth.LoginResponse;
import com.aide.backend.model.dto.auth.TokenResponse;
import com.aide.backend.model.dto.auth.UserProfileDTO;
import com.aide.backend.model.entity.user.AuthUserDetails;
import com.aide.backend.model.entity.user.User;
import com.aide.backend.model.entity.user.UserCredential;
import com.aide.backend.model.enums.CredentialType;
import com.aide.backend.model.enums.RoleEnum;
import com.aide.backend.repository.RoleRepository;
import com.aide.backend.repository.UserRepository;
import com.aide.backend.service.OAuth2Service;
import com.aide.backend.service.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

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
    public TokenResponse verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory()).setAudience(Collections.singletonList(googleClientId)).build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new BusinessException("Invalid ID token");
            }

            Payload payload = googleIdToken.getPayload();
            return processGoogleUser(payload);
        } catch (Exception e) {
            log.error("Error during token verification: {}", e.getMessage(), e);
            throw new BusinessException("Failed to verify Google token: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public LoginResponse handleGoogleAccessToken(String accessToken) {
        try {
            if (!StringUtils.hasText(accessToken)) {
                throw new BusinessException("Access token cannot be null or empty");
            }

            // Fetch user info from Google API
            RestTemplate restTemplate = new RestTemplate();
            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
            Map<String, Object> userInfo = restTemplate.getForObject(userInfoUrl, Map.class);

            if (userInfo == null) {
                throw new BusinessException("Failed to fetch user info from Google");
            }

            String email = (String) userInfo.get("email");
            String googleId = (String) userInfo.get("id");
            String name = (String) userInfo.get("name");
            String avatar = (String) userInfo.get("picture");

            // Create or get user
            User user = userRepository.findByEmail(email).orElseGet(() -> createGoogleUser(email, googleId, name, avatar));

            // Generate JWT tokens
            AuthUserDetails userDetails = new AuthUserDetails(
                    user.getUsername(),
                    null, user.isActive(),
                    true,
                    true,
                    true,
                    user.getRoles());

            String jwtAccessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            var loginResponse = new LoginResponse();
            loginResponse.setTokenResponse(TokenResponse.builder().accessToken(jwtAccessToken).refreshToken(refreshToken).tokenType("Bearer").expiresIn(24 * 60 * 60) // 24 hours in seconds
                    .build());
            loginResponse.setUserProfile(UserProfileDTO.builder().fullName(name).username(email).avtarUrl(avatar).build());
            return loginResponse;
        } catch (Exception e) {
            log.error("Error processing Google access token: {}", e.getMessage(), e);
            throw new BusinessException("Failed to process Google access token: " + e.getMessage());
        }
    }

    private TokenResponse processGoogleUser(Payload payload) {
        String email = payload.getEmail();
        String googleId = payload.getSubject();

        User user = userRepository.findByEmail(email).orElseGet(() -> createGoogleUser(email, googleId, (String) payload.get("name"), (String) payload.get("picture")));

        AuthUserDetails userDetails = new AuthUserDetails(
                user.getEmail(),
                null,
                user.isActive(),
                true,
                true,
                true,
                user.getRoles());

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).tokenType("Bearer").expiresIn(24 * 60 * 60) // 24 hours in seconds
                .build();
    }

    private User createGoogleUser(String email, String googleId, String name, String avatar) {
        var userRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER.toString()).orElseThrow(() -> new BusinessException("User role not found"));

        UserCredential googleCred = new UserCredential();
        googleCred.setCredType(CredentialType.OIDC);
        googleCred.setProvider("GOOGLE");
        googleCred.setOidcUserId(googleId);
        googleCred.setActive(true);

        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        user.setFullName(name);
        user.setAvatarUrl(avatar);
        user.setActive(true);
        user.getCredentials().add(googleCred);
        user.getRoles().add(userRole);
        googleCred.setUser(user);

        return userRepository.save(user);
    }
}
