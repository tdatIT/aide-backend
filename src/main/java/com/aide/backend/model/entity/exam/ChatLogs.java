package com.aide.backend.model.entity.exam;

import com.aide.backend.model.enums.SenderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "chat_logs")
public class ChatLogs {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private SenderType sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sess_id", nullable = false)
    private ExamSession session;

    LocalDateTime createdAt;
}
