package com.aide.backend.service;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.patients.ClinicalCateDTO;
import org.springframework.data.domain.Pageable;

public interface ClinicalCateService {
    void create(ClinicalCateDTO dto);
    void update(Long id, ClinicalCateDTO dto);
    void delete(Long id);
    ClinicalCateDTO findById(Long id);
    PageResponse<ClinicalCateDTO> findAll(Pageable pageable);
}
