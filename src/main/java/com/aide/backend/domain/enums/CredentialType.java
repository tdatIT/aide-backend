package com.aide.backend.domain.enums;

/**
 * Types of user credentials supported by the system.
 */
public enum CredentialType {
    /**
     * Standard password-based authentication
     */
    PASSWORD,

    /**
     * OpenID Connect authentication
     */
    OIDC,
}
