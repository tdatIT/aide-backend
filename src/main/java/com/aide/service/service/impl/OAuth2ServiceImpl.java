package com.aide.service.service.impl;

import com.aide.service.model.entity.User;
import com.aide.service.repository.UserRepository;
import com.aide.service.service.OAuth2Service;
import com.aide.service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public String processOAuthPostLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String googleId = oAuth2User.getAttribute("sub");

        User user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .googleId(googleId)
                            .provider(User.AuthProvider.GOOGLE)
                            .password("") // Google users don't need a password
                            .build();
                    return userRepository.save(newUser);
                });

        return jwtService.generateToken(user);
    }
} 