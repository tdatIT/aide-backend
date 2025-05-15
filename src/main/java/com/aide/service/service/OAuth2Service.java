package com.aide.service.service;

import com.aide.service.model.dto.auth.TokenResponse;

public interface OAuth2Service {
    TokenResponse verifyGoogleToken(String idToken);
} 