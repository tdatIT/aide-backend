package com.aide.backend.domain.dto.patients;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdatePatientTestRequest {
    @JsonProperty
    private Long patientId;
    private List<UpdateTestResultRequest> clinicalTests;
    private List<UpdateTestResultRequest> paraclinicalTests;
}
