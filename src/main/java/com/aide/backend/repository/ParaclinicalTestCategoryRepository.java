package com.aide.backend.repository;

import com.aide.backend.model.entity.patients.ParaclinicalTestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParaclinicalTestCategoryRepository extends JpaRepository<ParaclinicalTestCategory, Long> {
    boolean existsByName(String name);
} 