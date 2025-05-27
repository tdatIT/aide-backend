package com.aide.backend.service;

import com.aide.backend.model.dto.patients.CreatePatientCaseRequest;
import com.aide.backend.model.dto.patients.PatientCaseDTO;
import com.aide.backend.model.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface PatientCaseService {
    PatientCaseDTO create(CreatePatientCaseRequest request);
    PatientCaseDTO update(Long id, CreatePatientCaseRequest request);
    void delete(Long id);
    PatientCaseDTO findById(Long id);
    PageResponse<PatientCaseDTO> findAll(Pageable pageable);
    void updateStatus(Long id, boolean isPublished);
}
