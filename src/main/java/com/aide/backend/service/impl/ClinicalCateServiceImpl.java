package com.aide.backend.service.impl;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.patients.ClinicalCateDTO;
import com.aide.backend.domain.entity.patients.ClinicalCate;
import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.repository.ClinicalExamCategoryRepository;
import com.aide.backend.service.ClinicalCateService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClinicalCateServiceImpl implements ClinicalCateService {

    private final ClinicalExamCategoryRepository repository;

    @Override
    @Transactional
    public void create(ClinicalCateDTO dto) {
        ClinicalCate category = new ClinicalCate();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        repository.save(category);
    }

    @Override
    @Transactional
    public void update(Long id, ClinicalCateDTO dto) {
        ClinicalCate category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found with id: " + id));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        repository.save(category);
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
    public ClinicalCateDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical exam category not found with id: " + id));
    }

    @Override
    public PageResponse<ClinicalCateDTO> findAll(Pageable pageable) {
        Page<ClinicalCate> page = repository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToDTO));
    }

    private ClinicalCateDTO mapToDTO(ClinicalCate category) {
        return ClinicalCateDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
