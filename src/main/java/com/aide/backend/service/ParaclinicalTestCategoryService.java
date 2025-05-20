package com.aide.backend.service;

import com.aide.backend.model.dto.patients.ParaclinicalTestCategoryDTO;
import com.aide.backend.model.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ParaclinicalTestCategoryService {
    ParaclinicalTestCategoryDTO create(ParaclinicalTestCategoryDTO dto);
    ParaclinicalTestCategoryDTO update(Long id, ParaclinicalTestCategoryDTO dto);
    void delete(Long id);
    ParaclinicalTestCategoryDTO findById(Long id);
    PageResponse<ParaclinicalTestCategoryDTO> findAll(Pageable pageable);
} 