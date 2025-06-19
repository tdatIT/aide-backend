package com.aide.backend.repository;

import com.aide.backend.domain.entity.patients.ClinicalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalResultRepository extends JpaRepository<ClinicalResult, Long> {
}
