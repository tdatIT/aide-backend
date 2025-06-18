package com.aide.backend.repository;

import com.aide.backend.domain.entity.exams.ExamChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamLabTestRepository extends JpaRepository<ExamChoice, Long> {
}
