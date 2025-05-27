package com.aide.backend.controller.admin;

import com.aide.backend.model.dto.patients.CreatePatientCaseRequest;
import com.aide.backend.model.dto.patients.PatientCaseDTO;
import com.aide.backend.model.dto.common.PageResponse;
import com.aide.backend.model.dto.common.BaseResponse;
import com.aide.backend.service.PatientCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/patient-cases")
@RequiredArgsConstructor
public class PatientCaseController {

    private final PatientCaseService service;

    @PostMapping
    public ResponseEntity<BaseResponse<PatientCaseDTO>> create(@RequestBody CreatePatientCaseRequest request) {
        return ResponseEntity.ok(BaseResponse.success(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientCaseDTO>> update(@PathVariable Long id, @RequestBody CreatePatientCaseRequest request) {
        return ResponseEntity.ok(BaseResponse.success(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(BaseResponse.success("Patient case deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientCaseDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.success(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<PatientCaseDTO>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(service.findAll(pageable)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BaseResponse<Void>> updateStatus(@PathVariable Long id, @RequestParam boolean isPublished) {
        service.updateStatus(id, isPublished);
        return ResponseEntity.ok(BaseResponse.success("Patient case status updated successfully", null));
    }
}
