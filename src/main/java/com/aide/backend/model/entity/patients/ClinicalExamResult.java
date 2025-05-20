package com.aide.backend.model.entity.patients;

import com.aide.backend.common.Result;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clinical_exam_results")
@Getter
@Setter
public class ClinicalExamResult extends Result {
    @ManyToOne
    @JoinColumn(name = "clinical_exam_cate_id", nullable = false)
    private ClinicalExamCategory clinicalExamCategories;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;
}
