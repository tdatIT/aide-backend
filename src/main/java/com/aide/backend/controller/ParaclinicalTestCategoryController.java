package com.aide.backend.controller;

import com.aide.backend.common.BaseResponse;
import com.aide.backend.model.dto.patients.ParaclinicalTestCategoryDTO;
import com.aide.backend.service.ParaclinicalTestCategoryService;
import com.aide.backend.model.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/paraclinical-test-categories")
@RequiredArgsConstructor
@Tag(name = "Paraclinical Test Categories", description = "Paraclinical test category management endpoints")
public class ParaclinicalTestCategoryController {

    private final ParaclinicalTestCategoryService service;

    @PostMapping
    @Operation(summary = "Create a new paraclinical test category")
    public ResponseEntity<BaseResponse<ParaclinicalTestCategoryDTO>> create(@Valid @RequestBody ParaclinicalTestCategoryDTO dto) {
        return ResponseEntity.ok(BaseResponse.success(service.create(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a paraclinical test category")
    public ResponseEntity<BaseResponse<ParaclinicalTestCategoryDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ParaclinicalTestCategoryDTO dto) {
        return ResponseEntity.ok(BaseResponse.success(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a paraclinical test category")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(BaseResponse.success("Category deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a paraclinical test category by ID")
    public ResponseEntity<BaseResponse<ParaclinicalTestCategoryDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.success(service.findById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all paraclinical test categories with pagination")
    public ResponseEntity<BaseResponse<PageResponse<ParaclinicalTestCategoryDTO>>> findAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(BaseResponse.success(service.findAll(pageable)));
    }
}
