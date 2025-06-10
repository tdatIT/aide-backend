package com.aide.backend.model.entity.exam;

import com.aide.backend.common.BaseEntity;
import com.aide.backend.model.entity.patients.Patient;
import com.aide.backend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "exam_sessions")
public class ExamSession extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

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
    private List<ExamLabTest> exLabTest;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private ExamAnswers examAnswers;


}
