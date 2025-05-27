package com.aide.backend.model.entity.patients;

import com.aide.backend.common.BaseEntity;
import com.aide.backend.model.enums.Gender;
import com.aide.backend.model.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patients")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String occupation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(columnDefinition = "TEXT")
    private String dentalHistory;

    @Column(columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private String[] suggestedTests;

    @Column()
    private Long requestCounter=0L;

    @Column(nullable = false, length = 50)
    private String status = PatientStatus.UNPUBLISHED.toString();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
    private Set<ClinicalExamResult> clinicalExamResults = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
    private Set<ParaclinicalExamResult> paraclinicalExamResults = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "id")
    private Diagnosis diagnosis;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "treatment_id", referencedColumnName = "id")
    private Treatment treatment;

    private LocalDateTime deletedAt;
}
