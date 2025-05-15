package com.aide.service.model.entity;

import com.aide.service.common.BaseEntity;
import com.aide.service.model.enums.TestCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "test_results")
public class TestResult extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private MedicalVisit medicalVisit;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_category", nullable = false)
    private TestCategory testCategory;

    @Column(columnDefinition = "text")
    private String result;

    @Column(columnDefinition = "text")
    private String notes;
} 