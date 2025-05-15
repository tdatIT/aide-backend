package com.aide.backend.service;

public interface EmailService {
    void sendOTPEmail(String to, String otp);
} 