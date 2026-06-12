
package dev.trabalho.xfragil.entities.dto.reports_dtos;

import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomGenderDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRankingDTO;
import java.util.List;

public record GeneralReportUserDTO(
        
        List<SymptomRankingDTO> rankingSintomas,

        SymptomGenderDTO sintomaMaisMarcadoPorGenero,
        
        Long totalAvaliacoes,

        Double porcentagemTesteIndicado,

        Double porcentagemInconclusivo,
        
        Double scoreMedioAvaliacoes
        
    ) {}
