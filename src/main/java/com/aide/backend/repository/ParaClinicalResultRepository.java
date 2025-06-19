package com.aide.backend.repository;

import com.aide.backend.domain.entity.patients.ParaclinicalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParaClinicalResultRepository extends JpaRepository<ParaclinicalResult, Long> {
}
