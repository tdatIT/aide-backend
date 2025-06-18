package com.aide.backend.repository;

import com.aide.backend.domain.entity.exams.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExamSessionRepository extends JpaRepository<ExamSession, Long> {

    @Query("SELECT es FROM ExamSession es " +
            "WHERE es.patient.id = :patientId " +
            "AND es.user.id = :userId " +
            "AND es.expiresAt > CURRENT_TIMESTAMP " +
            "AND es.finishedAt IS NULL " +
            "ORDER BY es.createdAt DESC")
    Optional<ExamSession> findTopExamSessionByPatientId(Long patientId, Long userId);
}
