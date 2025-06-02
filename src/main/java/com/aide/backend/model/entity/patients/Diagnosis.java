package com.aide.backend.model.entity.patients;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "diagnoses")
public class Diagnosis extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diagnosis_name")
    private String diagnosisName;

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne(mappedBy = "diagnosis")
    private Patient patient;
}
