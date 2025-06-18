package com.aide.backend.domain.entity.patients;

import com.aide.backend.common.BaseEntity;
import com.aide.backend.domain.enums.Gender;
import com.aide.backend.domain.enums.Mode;
import com.aide.backend.domain.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Patient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String name;

    @Column(columnDefinition = "text")
    private String reasonForVisit;

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

    @Column(columnDefinition = "TEXT")
    private String clinicalHistory;

    @Column(columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private String[] suggestedTests;

    private Long requestCounter;

    @Column(nullable = false, length = 50)
    private String status;

    private Mode mode;

    @Column(columnDefinition = "TEXT")
    private String instruction;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
    private Set<ClinicalResult> clinicalExResults;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
    private Set<ParaclinicalResult> paraclinicalExResults;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "id")
    private Diagnosis diagnosis;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "treatment_id", referencedColumnName = "id")
    private Treatment treatment;

    private LocalDateTime deletedAt;

    public String getOpenAPIVariableForPatient() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"name\": \"").append(this.getName() != null ? this.getName() : "").append("\",\n");
        builder.append("  \"age\": \"").append(this.getAge() != null ? this.getAge().toString() : "").append("\",\n");
        builder.append("  \"gender\": \"").append(this.getGender() != null ? this.getGender().toString() : "").append("\",\n");
        builder.append("  \"reason_for_visit\": \"").append(this.getReasonForVisit() != null ? this.getReasonForVisit() : "").append("\",\n");
        builder.append("  \"occupation\": \"").append(this.getOccupation() != null ? this.getOccupation() : "").append("\",\n");
        builder.append("  \"medical_history\": \"").append(this.getMedicalHistory() != null ? this.getMedicalHistory() : "").append("\",\n");
        builder.append("  \"dental_history\": \"").append(this.getDentalHistory() != null ? this.getDentalHistory() : "").append("\",\n");
        builder.append("  \"clinical_history\": \"").append(this.getClinicalHistory() != null ? this.getClinicalHistory() : "").append("\",\n");
        builder.append("  \"instruction\": \"").append(this.getInstruction() != null ? this.getInstruction() : "").append("\",\n");
        builder.append("}");
        return builder.toString();
    }
}
