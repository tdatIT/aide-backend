package com.aide.backend.domain.entity.patients;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diagnoses")
public class Diagnosis extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String diagPrelim;

    @Column(columnDefinition = "text")
    private String diagDiff;

    @Column(columnDefinition = "text")
    private String notes;

    @OneToOne(mappedBy = "diagnosis")
    private Patient patient;
}
