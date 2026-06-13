
package dev.trabalho.xfragil.entities.dto.assessment_dtos;

public record AssessmentStatisticsDTO(
        
        Long totalAvaliacoes,
        
        Long avaliacoesProfissional,
        
        Long avaliacoesResponsavel,
        
        Long avaliacoesTesteIndicado,
        
        Long avaliacoesInconclusivas,
        
        Double mediaScore
        
    ) {}

