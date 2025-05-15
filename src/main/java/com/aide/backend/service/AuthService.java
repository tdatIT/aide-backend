package com.aide.backend.service;


import com.aide.backend.model.dto.auth.LoginRequest;
import com.aide.backend.model.dto.auth.RegisterRequest;
import com.aide.backend.model.dto.auth.TokenResponse;

public interface AuthService {
    void register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
    void logout(String token);
    TokenResponse refreshToken(String refreshToken);
}
