package com.aide.service.service.impl;

import com.aide.service.exception.BadRequestException;
import com.aide.service.model.entity.User;
import com.aide.service.model.entity.OTP;
import com.aide.service.model.entity.UserCredential;
import com.aide.service.model.enums.CredentialType;
import com.aide.service.repository.OTPRepository;
import com.aide.service.repository.UserCredentialRepository;
import com.aide.service.repository.UserRepository;
import com.aide.service.service.EmailService;
import com.aide.service.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final OTPRepository otpRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void sendPasswordResetOTP(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found with email: " + email));

        String otp = generateOTP();
        OTP otpEntity = OTP.builder()
                .email(email)
                .code(otp)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        otpRepository.save(otpEntity);
        emailService.sendOTPEmail(email, otp);
    }

    @Override
    @Transactional
    public void resetPassword(String email, String otp, String newPassword) {
        OTP otpEntity = otpRepository.findByEmailAndCodeAndUsedFalse(email, otp)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));

        if (otpEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        UserCredential cred = userCredentialRepository.findByUserIdAndCredType(user.getId(), CredentialType.PASSWORD)
                .orElseThrow(() -> new BadRequestException("Credential not found"));

        cred.setPassword(passwordEncoder.encode(newPassword));
        userCredentialRepository.save(cred);

        otpEntity.setUsed(true);
        otpRepository.save(otpEntity);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
} 