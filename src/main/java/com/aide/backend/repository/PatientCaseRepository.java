package com.aide.backend.repository;

import com.aide.backend.model.entity.patients.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientCaseRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByStatus(Integer status, Pageable pageable);
    boolean existsByIdAndStatus(Long id, Integer status);
} 