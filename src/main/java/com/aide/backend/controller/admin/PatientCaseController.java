package com.aide.backend.controller.admin;

import com.aide.backend.domain.dto.common.BaseResponse;
import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.patients.CreatePatientRequest;
import com.aide.backend.domain.dto.patients.PatientDTO;
import com.aide.backend.domain.dto.patients.UpdatePatientRequest;
import com.aide.backend.service.PatientService;
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

    private final PatientService service;

    @PostMapping
    public ResponseEntity<BaseResponse<PatientDTO>> create(@RequestBody CreatePatientRequest request) {
        service.create(request);
        return ResponseEntity.ok(BaseResponse.success("success", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientDTO>> update(@PathVariable Long id, @RequestBody UpdatePatientRequest request) {
        service.update(request);
        return ResponseEntity.ok(BaseResponse.success("success", null));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        service.softDeleteById(id);
        return ResponseEntity.ok(BaseResponse.success("Patient case deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PatientDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.success(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<PatientDTO>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(service.findAll(pageable)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BaseResponse<Void>> updateStatus(@PathVariable Long id, @RequestParam boolean isPublished) {
        service.updateStatus(id, isPublished);
        return ResponseEntity.ok(BaseResponse.success("Patient case status updated successfully", null));
    }
}
