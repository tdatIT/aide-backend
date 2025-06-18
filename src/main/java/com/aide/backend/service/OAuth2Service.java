package com.aide.backend.service;

import com.aide.backend.domain.dto.auth.LoginResponse;

public interface OAuth2Service {
    LoginResponse handleGoogleIdToken(String idToken);
}
