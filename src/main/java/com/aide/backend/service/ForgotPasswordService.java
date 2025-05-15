package com.aide.backend.service;

public interface ForgotPasswordService {
    void sendPasswordResetOTP(String email);
    void resetPassword(String email, String otp, String newPassword);
} 