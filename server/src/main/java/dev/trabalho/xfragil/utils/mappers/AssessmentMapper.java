package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.utils.enums.Result;
import dev.trabalho.xfragil.utils.enums.Origin;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper {
    
    public AssessmentResponseDTO toDto(Assessment a) {
        return new AssessmentResponseDTO(
                a.getId(),
                a.getPatient().getName(),
                a.getUser() != null ? a.getUser().getName() : "Realizada por responsável",
                a.getOrigin(),
                a.getAssessmentDate(),
                a.getScore(),
                a.getResult(),
                a.getDetails(),
                a.getDnaTest(),
                a.getExamInterest(),
                a.getExamResult(),
                a.getAutismDiagnosis(),
                a.getHasSiblings(),
                a.getDisabilityHistory(),
                a.getMenopauseHistory(),
                a.getAtaxiaHistory()
        );
    }
    
    public Assessment toAssessment(
            Patient patient,
            Users user,
            Origin origin,
            BigDecimal score,
            Result result,
            AssessmentRequestDTO assessmentRequest
    ) {
        Assessment assessment = new Assessment(
                patient,
                user,
                origin,
                score,
                result
        );
        assessment.setDetails(assessmentRequest.detalhes());
        assessment.setDnaTest(assessmentRequest.testeDna());
        assessment.setExamInterest(assessmentRequest.interesseExame());
        assessment.setExamResult(assessmentRequest.resultadoExame());
        assessment.setAutismDiagnosis(assessmentRequest.diagnosticoAutismo());
        assessment.setHasSiblings(assessmentRequest.possuiIrmaos());
        assessment.setDisabilityHistory(assessmentRequest.antecedentesDeficiencia());
        assessment.setMenopauseHistory(assessmentRequest.antecedentesMenopausa());
        assessment.setAtaxiaHistory(assessmentRequest.antecedentesAtaxia());
        return assessment;
    }
}
