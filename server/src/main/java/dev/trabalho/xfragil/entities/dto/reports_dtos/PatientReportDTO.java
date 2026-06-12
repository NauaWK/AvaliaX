
package dev.trabalho.xfragil.entities.dto.reports_dtos;

import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRankingDTO;
import java.util.List;

public record PatientReportDTO(
    
        String nomePaciente,
        
        Integer idade,
        
        String genero,
        
        String nomeMae,
        
        String nomePai,
        
        Long totalAvaliacoes,
        
        Long avaliacoesProfissional,
        
        Long avaliacoesResponsavel,
        
        Double mediaScore,
        
        List<SymptomRankingDTO> top3Sintomas
        
    ) {}


