package com.aide.service.service;


import com.aide.service.model.dto.auth.LoginRequest;
import com.aide.service.model.dto.auth.RegisterRequest;
import com.aide.service.model.dto.auth.TokenResponse;

public interface AuthService {
    void register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
    void logout(String token);
    TokenResponse refreshToken(String refreshToken);
}
