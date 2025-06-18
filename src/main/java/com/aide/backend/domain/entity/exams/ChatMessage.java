package com.aide.backend.domain.entity.exams;

import com.aide.backend.common.BaseEntity;
import com.aide.backend.domain.enums.SenderType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String openaiMsgId;

    private SenderType sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sess_id", nullable = false)
    private ExamSession session;
}
