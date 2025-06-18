package com.aide.backend.domain.entity.patients;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "clinical_results")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String textResult;

    @Column(columnDefinition = "text")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "clinical_cate_id", nullable = false)
    private ClinicalCate clinicalCategories;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "clinical_result_imgs",
            joinColumns = @JoinColumn(name = "clinical_exam_result_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<Image> images;
}
