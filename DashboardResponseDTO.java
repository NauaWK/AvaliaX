package dev.trabalho.xfragil.entities.dto.response_dto;

import java.util.List;

public record DashboardResponseDTO(

        Long totalPacientes,
        Long totalAvaliacoes,
        List<RecentAssessmentResponseDTO> avaliacoesRecentes

) {}