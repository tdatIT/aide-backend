package com.aide.backend.controller.admin;

import com.aide.backend.common.BaseResponse;
import com.aide.backend.model.dto.patients.ClinicalExamCategoryDTO;
import com.aide.backend.service.ClinicalExamCategoryService;
import com.aide.backend.model.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/clinical-ex-cats")
@RequiredArgsConstructor
@Tag(name = "Clinical Exam Categories", description = "Clinical exam category management endpoints")
public class ClinicalExCatController {

    private final ClinicalExamCategoryService service;

    @PostMapping
    @Operation(summary = "Create a new clinical exam category")
    public ResponseEntity<BaseResponse<ClinicalExamCategoryDTO>> create(@Valid @RequestBody ClinicalExamCategoryDTO dto) {
        return ResponseEntity.ok(BaseResponse.success(service.create(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a clinical exam category")
    public ResponseEntity<BaseResponse<ClinicalExamCategoryDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ClinicalExamCategoryDTO dto) {
        return ResponseEntity.ok(BaseResponse.success(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a clinical exam category")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(BaseResponse.success("Category deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a clinical exam category by ID")
    public ResponseEntity<BaseResponse<ClinicalExamCategoryDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.success(service.findById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all clinical exam categories with pagination")
    public ResponseEntity<BaseResponse<PageResponse<ClinicalExamCategoryDTO>>> findAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(BaseResponse.success(service.findAll(pageable)));
    }
}
