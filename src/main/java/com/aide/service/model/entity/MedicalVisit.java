package com.aide.service.model.entity;

import com.aide.service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "medical_visits")
public class MedicalVisit extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(columnDefinition = "text")
    private String reason;

    @Column(name = "general_history", columnDefinition = "text")
    private String generalHistory;

    @Column(name = "dental_history", columnDefinition = "text")
    private String dentalHistory;

    @Column(name = "disease_progress", columnDefinition = "text")
    private String diseaseProgress;

    @Column(name = "general_exam", columnDefinition = "text")
    private String generalExam;

    @Column(name = "facial_exam", columnDefinition = "text")
    private String facialExam;

    @Column(name = "oral_exam", columnDefinition = "text")
    private String oralExam;

    @Column(name = "additional_notes", columnDefinition = "text")
    private String additionalNotes;

    @OneToMany(mappedBy = "medicalVisit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults = new ArrayList<>();

    @OneToMany(mappedBy = "medicalVisit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(mappedBy = "medicalVisit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Treatment> treatments = new ArrayList<>();
} 