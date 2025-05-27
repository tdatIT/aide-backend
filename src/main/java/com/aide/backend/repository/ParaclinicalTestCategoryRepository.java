package com.aide.backend.repository;

import com.aide.backend.model.entity.patients.ParaclinicalTestCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParaclinicalTestCategoryRepository extends JpaRepository<ParaclinicalTestCategory, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ParaclinicalTestCategory c WHERE c.name = :name AND c.deletedAt IS NULL")
    boolean existsByName(String name);

    @Query("SELECT c FROM ParaclinicalTestCategory c WHERE c.deletedAt IS NULL")
    Page<ParaclinicalTestCategory> findAll(Pageable pageable);
} 