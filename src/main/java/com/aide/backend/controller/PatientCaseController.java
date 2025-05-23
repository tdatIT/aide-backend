package com.aide.backend.controller;

import com.aide.backend.model.dto.patients.CreatePatientCaseRequest;
import com.aide.backend.model.dto.patients.PatientCaseDTO;
import com.aide.backend.model.dto.common.PageResponse;
import com.aide.backend.service.PatientCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient-cases")
@RequiredArgsConstructor
public class PatientCaseController {

    private final PatientCaseService service;

    @PostMapping
    public ResponseEntity<PatientCaseDTO> create(@RequestBody CreatePatientCaseRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientCaseDTO> update(@PathVariable Long id, @RequestBody CreatePatientCaseRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientCaseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PatientCaseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }
} 