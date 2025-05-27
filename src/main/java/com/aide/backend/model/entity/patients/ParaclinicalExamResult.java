package com.aide.backend.model.entity.patients;

import com.aide.backend.common.Result;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paraclinical_exam_results")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParaclinicalExamResult extends Result {
    @ManyToOne
    @JoinColumn(name = "paraclinical_test_cate_id", nullable = false)
    private ParaclinicalTestCategory paraclinicalTestCategory;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;
}
