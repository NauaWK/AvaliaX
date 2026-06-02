
package dev.trabalho.xfragil.entities.dto.autoassessment_dtos;

import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTOUser;

public record AutoAssessmentRequestDTO(
        
    PatientRequestDTOUser paciente,
    GuardianRequestDTO responsavel,
    AssessmentRequestDTO avaliacao
        
    ) {}

