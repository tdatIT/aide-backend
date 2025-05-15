package com.aide.service.repository;

import com.aide.service.model.entity.UserCredential;
import com.aide.service.model.enums.CredentialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByUserIdAndCredType(Long userId, CredentialType credType);

    @Query("select cred from UserCredential cred where cred.user.username = :username and cred.credType = :credType and  cred.active = true")
    Optional<UserCredential> findByUsernameAndCredType(String username, CredentialType credType);
} 