package com.aide.backend.repository;

import com.aide.backend.domain.entity.exams.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE m.session.id = :examSessionId ORDER BY m.createdAt DESC LIMIT 1")
    Optional<ChatMessage> findLastByExamSessionId(Long examSessionId);

    @Query("SELECT m FROM ChatMessage m WHERE m.session.id = :examSessionId ORDER BY m.createdAt ASC")
    List<ChatMessage> findChatLogsBySessionId(Long examSessionId);
}

