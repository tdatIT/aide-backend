package com.aide.backend.repository;

import com.aide.backend.model.entity.patients.ClinicalExamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalExamCategoryRepository extends JpaRepository<ClinicalExamCategory, Long> {
    boolean existsByName(String name);
} 