package com.aide.backend.service;


import com.aide.backend.domain.dto.auth.LoginRequest;
import com.aide.backend.domain.dto.auth.RegisterRequest;
import com.aide.backend.domain.dto.auth.TokenResponse;

public interface AuthService {
    void register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
    void logout(String token);
    TokenResponse refreshToken(String refreshToken);
}
