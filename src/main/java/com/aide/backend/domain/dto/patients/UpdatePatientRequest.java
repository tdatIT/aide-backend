package com.aide.backend.domain.dto.patients;

import com.aide.backend.domain.enums.Gender;
import com.aide.backend.domain.enums.Mode;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdatePatientRequest {
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
    private List<UpdateTestResultRequest> clinicalExams;
    private List<UpdateTestResultRequest> paraclinicalTests;
    private DiagnosisDTO diagnosis;
    private TreatmentDTO treatment;
}
