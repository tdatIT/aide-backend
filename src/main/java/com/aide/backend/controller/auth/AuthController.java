package com.aide.backend.controller.auth;

import com.aide.backend.common.BaseResponse;
import com.aide.backend.model.dto.auth.*;
import com.aide.backend.service.AuthService;
import com.aide.backend.service.ForgotPasswordService;
import com.aide.backend.service.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<BaseResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(BaseResponse.<Void>success("User registered successfully", null));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get tokens")
    public ResponseEntity<BaseResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(BaseResponse.success(tokenResponse));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Get new access token using refresh token")
    public ResponseEntity<BaseResponse<TokenResponse>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        TokenResponse tokenResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(BaseResponse.success(tokenResponse));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and invalidate token")
    public ResponseEntity<BaseResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        authService.logout(token);
        return ResponseEntity.ok(BaseResponse.success("Logged out successfully", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> sendPasswordResetOTP(@RequestParam String email) {
        forgotPasswordService.sendPasswordResetOTP(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        forgotPasswordService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/oauth2/google")
    @Operation(summary = "Authenticate with Google ID token")
    public ResponseEntity<BaseResponse<LoginResponse>> googleLogin(@RequestParam String token) {
        LoginResponse res = oAuth2Service.handleGoogleAccessToken(token);
        return ResponseEntity.ok(BaseResponse.success(res));
    }
}
