package com.aide.backend.service;

import com.aide.backend.model.dto.patients.ClinicalExamCategoryDTO;
import com.aide.backend.model.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ClinicalExamCategoryService {
    ClinicalExamCategoryDTO create(ClinicalExamCategoryDTO dto);
    ClinicalExamCategoryDTO update(Long id, ClinicalExamCategoryDTO dto);
    void delete(Long id);
    ClinicalExamCategoryDTO findById(Long id);
    PageResponse<ClinicalExamCategoryDTO> findAll(Pageable pageable);
} 