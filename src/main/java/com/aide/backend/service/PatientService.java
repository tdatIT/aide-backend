package com.aide.backend.service;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.patients.CreatePatientRequest;
import com.aide.backend.domain.dto.patients.PatientDTO;
import com.aide.backend.domain.dto.patients.UpdatePatientRequest;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    void create(CreatePatientRequest request);
    void update(UpdatePatientRequest request);
    void softDeleteById(Long id);
    PatientDTO findById(Long id);
    PageResponse<PatientDTO> findAll(Pageable pageable);
    void updateStatus(Long id, boolean isPublished);
}
