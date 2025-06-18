package com.aide.backend.domain.entity.exams;

import com.aide.backend.common.BaseEntity;
import com.aide.backend.domain.entity.patients.ClinicalCate;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "exam_choices")
public class ExamChoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private Long score;

    @ManyToOne
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession session;

    @ManyToOne
    @JoinColumn(name = "clinical_ex_cate_id", nullable = false)
    private ClinicalCate clinicalExCat;

    @ManyToOne
    @JoinColumn(name = "para_test_cate_id", nullable = false)
    private ClinicalCate paraTestCat;
}
