package com.aide.service.service;

public interface ForgotPasswordService {
    void sendPasswordResetOTP(String email);
    void resetPassword(String email, String otp, String newPassword);
} 