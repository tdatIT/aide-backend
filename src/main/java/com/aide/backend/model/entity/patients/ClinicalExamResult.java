package com.aide.backend.model.entity.patients;

import com.aide.backend.common.Result;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "clinical_exam_results")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalExamResult extends Result {
    @ManyToOne
    @JoinColumn(name = "clinical_exam_cate_id", nullable = false)
    private ClinicalExamCategory clinicalExamCategories;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "clinical_exam_result_images",
        joinColumns = @JoinColumn(name = "clinical_exam_result_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<Image> images;
}
