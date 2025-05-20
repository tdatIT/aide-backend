package com.aide.backend.service.impl;

import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.model.dto.patients.ParaclinicalTestCategoryDTO;
import com.aide.backend.model.dto.common.PageResponse;
import com.aide.backend.model.entity.patients.ParaclinicalTestCategory;
import com.aide.backend.repository.ParaclinicalTestCategoryRepository;
import com.aide.backend.service.ParaclinicalTestCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParaclinicalTestCategoryServiceImpl implements ParaclinicalTestCategoryService {

    private final ParaclinicalTestCategoryRepository repository;

    @Override
    @Transactional
    public ParaclinicalTestCategoryDTO create(ParaclinicalTestCategoryDTO dto) {
        ParaclinicalTestCategory category = new ParaclinicalTestCategory();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return mapToDTO(repository.save(category));
    }

    @Override
    @Transactional
    public ParaclinicalTestCategoryDTO update(Long id, ParaclinicalTestCategoryDTO dto) {
        ParaclinicalTestCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test category not found with id: " + id));
        
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return mapToDTO(repository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Paraclinical test category not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public ParaclinicalTestCategoryDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test category not found with id: " + id));
    }

    @Override
    public PageResponse<ParaclinicalTestCategoryDTO> findAll(Pageable pageable) {
        Page<ParaclinicalTestCategory> page = repository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToDTO));
    }

    private ParaclinicalTestCategoryDTO mapToDTO(ParaclinicalTestCategory category) {
        ParaclinicalTestCategoryDTO dto = new ParaclinicalTestCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
} 