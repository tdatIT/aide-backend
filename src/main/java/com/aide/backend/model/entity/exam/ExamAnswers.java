package com.aide.backend.model.entity.exam;

import com.aide.backend.common.BaseEntity;
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
@Table(name = "exam_answers")
public class ExamAnswers extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnosis;

    private Long score;

    private String gptFeedback;

    private LocalDateTime evaluatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    private ExamSession session;
}
