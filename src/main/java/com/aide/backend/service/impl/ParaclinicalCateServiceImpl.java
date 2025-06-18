package com.aide.backend.service.impl;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.patients.ParaclinicalCateDTO;
import com.aide.backend.domain.entity.patients.ParaclinicalCate;
import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.repository.ParaclinicalCateRepository;
import com.aide.backend.service.ParaclinicalCateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParaclinicalCateServiceImpl implements ParaclinicalCateService {

    private final ParaclinicalCateRepository repository;

    @Override
    @Transactional
    public void create(ParaclinicalCateDTO dto) {
        ParaclinicalCate category = new ParaclinicalCate();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        repository.save(category);
    }

    @Override
    @Transactional
    public void update(Long id, ParaclinicalCateDTO dto) {
        ParaclinicalCate category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test category not found with id: " + id));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        repository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test category not found with id: " + id));
        category.setDeletedAt(LocalDateTime.now());

        repository.save(category);
    }

    @Override
    public ParaclinicalCateDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Paraclinical test category not found with id: " + id));
    }

    @Override
    public PageResponse<ParaclinicalCateDTO> findAll(Pageable pageable) {
        Page<ParaclinicalCate> page = repository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToDTO));
    }

    private ParaclinicalCateDTO mapToDTO(ParaclinicalCate category) {
        ParaclinicalCateDTO dto = new ParaclinicalCateDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
