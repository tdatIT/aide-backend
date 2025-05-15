package com.aide.service.model.entity;

import com.aide.service.common.BaseEntity;
import com.aide.service.model.enums.CredentialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_credentials")
@AllArgsConstructor
@NoArgsConstructor
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
    private String provider;

    @Column(length = 100, nullable = false)
    private String oidcUserId;

    @Column(length = 200)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "attempt_count")
    private Integer attemptCount;
} 