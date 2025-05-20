package com.aide.backend.service.impl;

import com.aide.backend.exception.BadRequestException;
import com.aide.backend.model.entity.user.User;
import com.aide.backend.model.entity.user.OTP;
import com.aide.backend.model.entity.user.UserCredential;
import com.aide.backend.model.enums.CredentialType;
import com.aide.backend.repository.OTPRepository;
import com.aide.backend.repository.UserCredentialRepository;
import com.aide.backend.repository.UserRepository;
import com.aide.backend.service.EmailService;
import com.aide.backend.service.ForgotPasswordService;
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
