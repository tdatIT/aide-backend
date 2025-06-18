package com.aide.backend.domain.entity.patients;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paraclinical_results")
public class ParaclinicalResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String textResult;

    @Column(columnDefinition = "text")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "paraclinical_test_cate_id", nullable = false)
    private ParaclinicalCate paraclinicalTestCategory;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "parac_result_imgs",
        joinColumns = @JoinColumn(name = "parac_result_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<Image> images;
}
