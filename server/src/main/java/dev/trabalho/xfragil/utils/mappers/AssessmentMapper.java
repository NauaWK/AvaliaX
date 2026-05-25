
package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper {
    
    public AssessmentResponseDTO toDto(Assessment a)
    {
        return new AssessmentResponseDTO(
                a.getId(),
                a.getPatient().getName(),
                a.getUser().getName(),
                a.getAssessmentDate(),
                a.getScore(),
                a.getResult(),
                a.getDetails()
        );
    }
    
}
