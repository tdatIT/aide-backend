package com.aide.backend.domain.entity.exams;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exam_answers")
public class ExamAnswers extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnosis;

    private Long score;

    private String aiFeedback;

    @OneToOne(fetch = FetchType.LAZY)
    private ExamSession session;
}
