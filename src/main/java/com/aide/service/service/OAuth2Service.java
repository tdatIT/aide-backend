package com.aide.service.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Service {
    String processOAuthPostLogin(OAuth2User oAuth2User);
} 