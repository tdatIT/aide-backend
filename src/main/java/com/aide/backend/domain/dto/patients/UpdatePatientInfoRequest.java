package com.aide.backend.domain.dto.patients;

import com.aide.backend.domain.enums.Gender;
import com.aide.backend.domain.enums.Mode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePatientInfoRequest {
    private Long id;
    private String name;
    private Gender gender;
    private Integer age;
    private String occupation;
    private Mode mode;
    private String reasonForVisit;
    private String medicalHistory;
    private String dentalHistory;
    private String clinicalHistory;
    private String[] suggestedTests;
    private String instruction;
    private DiagnosisDTO diagnosis;
    private TreatmentDTO treatment;
}
