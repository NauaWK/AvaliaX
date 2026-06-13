
package dev.trabalho.xfragil.entities.dto.reports_dtos;

import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentOverviewDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentStatisticsDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientGeneralDataDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRankingDTO;
import java.util.List;

public record PatientReportDTO(
    
        PatientGeneralDataDTO dadosPaciente,
        
        AssessmentStatisticsDTO dadosAvaliacoesPaciente,
        
        List<AssessmentOverviewDTO> avaliacoesRecentes,
        
        List<SymptomRankingDTO> sintomasMaisPresentes
        
    ) {}


