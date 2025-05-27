package com.aide.backend.model.entity.patients;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "paraclinical_test_categories")
@Getter
@Setter
public class ParaclinicalTestCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    private LocalDateTime deletedAt;
}
