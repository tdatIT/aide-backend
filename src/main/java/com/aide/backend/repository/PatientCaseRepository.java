package com.aide.backend.repository;

import com.aide.backend.model.entity.patients.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientCaseRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.status = :status AND p.deletedAt IS NULL")
    Page<Patient> findByStatus(Integer status, Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Patient p WHERE p.id = :id AND p.status = :status AND p.deletedAt IS NULL")
    boolean existsByIdAndStatus(Long id, Integer status);

    @Query("SELECT p FROM Patient p WHERE p.deletedAt IS NULL")
    Page<Patient> findAll(Pageable pageable);
} 