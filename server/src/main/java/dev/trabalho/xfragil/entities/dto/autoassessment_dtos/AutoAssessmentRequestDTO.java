
package dev.trabalho.xfragil.entities.dto.autoassessment_dtos;

import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import jakarta.validation.Valid;

public record AutoAssessmentRequestDTO(
        
        @Valid
        PatientRequestDTO paciente,

        @Valid
        GuardianRequestDTO responsavel,

        @Valid
        AssessmentRequestDTO avaliacao
        
    ) {}

