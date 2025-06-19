package com.aide.backend.controller.admin;

import com.aide.backend.common.BaseResponse;
import com.aide.backend.common.PageResponse;
import com.aide.backend.domain.dto.patients.ParaclinicalCateDTO;
import com.aide.backend.service.ParaclinicalCateService;
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
@RequestMapping("/api/v1/admin/paraclinical-test-cats")
@RequiredArgsConstructor
@Tag(name = "Paraclinical Test Categories", description = "Paraclinical test category management endpoints")
public class ParaclinicalTestCatController {

    private final ParaclinicalCateService service;

    @PostMapping
    @Operation(summary = "Create a new paraclinical test category")
    public ResponseEntity<BaseResponse<ParaclinicalCateDTO>> create(@Valid @RequestBody ParaclinicalCateDTO dto) {
        service.create(dto);
        return ResponseEntity.ok(BaseResponse.success("success", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a paraclinical test category")
    public ResponseEntity<BaseResponse<ParaclinicalCateDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ParaclinicalCateDTO dto) {
        service.update(id, dto);
        return ResponseEntity.ok(BaseResponse.success("success", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a paraclinical test category")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(BaseResponse.success("Category deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a paraclinical test category by ID")
    public ResponseEntity<BaseResponse<ParaclinicalCateDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.success(service.findById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all paraclinical test categories with pagination")
    public ResponseEntity<BaseResponse<PageResponse<ParaclinicalCateDTO>>> findAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(BaseResponse.success(service.findAll(pageable)));
    }
}
