
package dev.trabalho.xfragil.entities.dto.assessment_dtos;

import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRequestDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record AssessmentRequestDTO(
        
        @NotNull(message = "ID do paciente é obrigatório!")
        Integer id_paciente,
        
        @Size(max = 255, message = "Detalhes não podem passar de 255 caracteres!")
        String detalhes,
        
        List<SymptomRequestDTO> sintomas
        
    ) {}
