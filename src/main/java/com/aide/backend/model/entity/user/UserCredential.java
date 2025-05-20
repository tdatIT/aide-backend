package com.aide.backend.model.entity.user;

import com.aide.backend.common.BaseEntity;
import com.aide.backend.model.enums.CredentialType;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "cred_type", length = 20, nullable = false)
    private CredentialType credType;

    @Column(length = 100)
    private String provider;

    @Column(length = 100)
    private String oidcUserId;

    @Column(length = 200)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "attempt_count")
    private Integer attemptCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
