package com.aide.backend.service;

import com.aide.backend.model.dto.auth.LoginResponse;
import com.aide.backend.model.dto.auth.TokenResponse;

public interface OAuth2Service {
    TokenResponse verifyGoogleToken(String idToken);
    LoginResponse handleGoogleAccessToken(String accessToken);
}
