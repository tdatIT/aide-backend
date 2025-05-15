package com.aide.service.model.entity;

import com.aide.service.common.BaseEntity;
import com.aide.service.model.enums.CredentialType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_credentials")
public class UserCredential extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "cred_type", length = 20, nullable = false)
    private CredentialType credType;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 1000, nullable = false)
    private String secret;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "attempt_count")
    private Integer attemptCount;
} 