package com.aide.backend.service.impl;

import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.model.dto.patients.ClinicalExamCategoryDTO;
import com.aide.backend.model.dto.common.PageResponse;
import com.aide.backend.model.entity.patients.ClinicalExamCategory;
import com.aide.backend.repository.ClinicalExamCategoryRepository;
import com.aide.backend.service.ClinicalExamCategoryService;
import com.google.api.client.util.DateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClinicalExamCategoryServiceImpl implements ClinicalExamCategoryService {

    private final ClinicalExamCategoryRepository repository;

    @Override
    @Transactional
    public ClinicalExamCategoryDTO create(ClinicalExamCategoryDTO dto) {
        ClinicalExamCategory category = new ClinicalExamCategory();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return mapToDTO(repository.save(category));
    }

    @Override
    @Transactional
    public ClinicalExamCategoryDTO update(Long id, ClinicalExamCategoryDTO dto) {
        ClinicalExamCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found with id: " + id));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return mapToDTO(repository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found with id: " + id));
        category.setDeletedAt(LocalDateTime.now());
        repository.save(category);
    }

    @Override
    public ClinicalExamCategoryDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found with id: " + id));
    }

    @Override
    public PageResponse<ClinicalExamCategoryDTO> findAll(Pageable pageable) {
        Page<ClinicalExamCategory> page = repository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToDTO));
    }

    private ClinicalExamCategoryDTO mapToDTO(ClinicalExamCategory category) {
        ClinicalExamCategoryDTO dto = new ClinicalExamCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
