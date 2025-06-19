package com.aide.backend.service;

import com.aide.backend.common.PageResponse;
import com.aide.backend.domain.dto.patients.CreatePatientRequest;
import com.aide.backend.domain.dto.patients.PatientDTO;
import com.aide.backend.domain.dto.patients.UpdatePatientInfoRequest;
import com.aide.backend.domain.dto.patients.UpdatePatientTestRequest;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    void create(CreatePatientRequest request);
    void updatePatientInfo(UpdatePatientInfoRequest request);
    void updatePatientTest(UpdatePatientTestRequest request);
    void softDeleteById(Long id);
    PatientDTO findById(Long id);
    PageResponse<PatientDTO> findAll(Pageable pageable);
    void updateStatus(Long id, boolean isPublished);
}
