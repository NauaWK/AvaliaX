
package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.utils.enums.Result;
import java.math.BigDecimal;
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
    
    public Assessment toAssessment(Patient p, Users u, BigDecimal score, Result r, String details)
    {
        return new Assessment(
                p,
                u,
                score,
                r,
                details
        );
    }
    
}
