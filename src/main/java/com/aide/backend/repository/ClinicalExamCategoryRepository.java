package com.aide.backend.repository;

import com.aide.backend.domain.entity.patients.ClinicalCate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalExamCategoryRepository extends JpaRepository<ClinicalCate, Long> {
    @Query("SELECT c FROM ClinicalCate c WHERE c.deletedAt IS NULL")
    Page<ClinicalCate> findAll(Pageable pageable);
}
