package com.aide.backend.domain.entity.exams;

import com.aide.backend.common.BaseEntity;
import com.aide.backend.domain.entity.patients.Patient;
import com.aide.backend.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "exam_sessions")
public class ExamSession extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String lastedMessageId;

    private LocalDateTime startedAt;

    private LocalDateTime expiresAt;

    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<ExamChoice> exLabTest;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private ExamAnswers examAnswers;

}
