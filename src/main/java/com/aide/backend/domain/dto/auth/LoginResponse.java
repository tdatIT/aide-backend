package com.aide.backend.domain.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class LoginResponse extends TokenResponse {
    private UserProfileDTO profile;
}
