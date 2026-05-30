
package dev.trabalho.xfragil.entities.dto.assessment_dtos;

import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRequestDTO;
import dev.trabalho.xfragil.utils.customAnnotations.CPF_annotation.CPF;
import jakarta.validation.constraints.Size;
import java.util.List;

public record AssessmentRequestDTO(
        
        @CPF
        String CPF_paciente,
        
        @Size(max = 255, message = "Detalhes não podem passar de 255 caracteres!")
        String detalhes,
        
        List<SymptomRequestDTO> sintomas
        
    ) {}
