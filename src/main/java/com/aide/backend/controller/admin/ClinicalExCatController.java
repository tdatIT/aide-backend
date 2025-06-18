package com.aide.backend.controller.admin;

import com.aide.backend.common.BaseResponse;
import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.patients.ClinicalCateDTO;
import com.aide.backend.service.ClinicalCateService;
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

    private final ClinicalCateService service;

    @PostMapping
    @Operation(summary = "Create a new clinical exam category")
    public ResponseEntity<BaseResponse<ClinicalCateDTO>> create(@Valid @RequestBody ClinicalCateDTO dto) {
        service.create(dto);
        return ResponseEntity.ok(BaseResponse.success("success", null));

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a clinical exam category")
    public ResponseEntity<BaseResponse<ClinicalCateDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ClinicalCateDTO dto) {
        service.update(id, dto);
        return ResponseEntity.ok(BaseResponse.success("success", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a clinical exam category")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(BaseResponse.success("success", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a clinical exam category by ID")
    public ResponseEntity<BaseResponse<ClinicalCateDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.success(service.findById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all clinical exam categories with pagination")
    public ResponseEntity<BaseResponse<PageResponse<ClinicalCateDTO>>> findAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(BaseResponse.success(service.findAll(pageable)));
    }
}
