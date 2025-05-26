package com.aide.backend.model.entity.patients;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "treatments")
public class Treatment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String notes;

    @OneToOne(mappedBy = "treatment")
    private Patient patient;
}

