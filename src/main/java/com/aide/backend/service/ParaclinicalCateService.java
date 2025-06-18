package com.aide.backend.service;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.patients.ParaclinicalCateDTO;
import org.springframework.data.domain.Pageable;

public interface    ParaclinicalCateService {
    void create(ParaclinicalCateDTO dto);
    void update(Long id, ParaclinicalCateDTO dto);
    void delete(Long id);
    ParaclinicalCateDTO findById(Long id);
    PageResponse<ParaclinicalCateDTO> findAll(Pageable pageable);
}
