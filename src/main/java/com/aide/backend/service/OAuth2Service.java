package com.aide.backend.service;

import com.aide.backend.model.dto.auth.TokenResponse;

public interface OAuth2Service {
    TokenResponse verifyGoogleToken(String idToken);
} 