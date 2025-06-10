package com.aide.backend.model.entity.exam;

import com.aide.backend.model.entity.patients.ClinicalExamCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "exam_lab_tests")
public class ExamLabTest {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private Long score;

    @ManyToOne
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSession session;

    @ManyToOne
    @JoinColumn(name = "clinical_ex_cate_id", nullable = false)
    private ClinicalExamCategory clinicalExCat;

    @ManyToOne
    @JoinColumn(name = "para_test_cate_id", nullable = false)
    private ClinicalExamCategory paraTestCat;
}
