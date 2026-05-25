package dev.trabalho.xfragil.entities.dto.dashboard_dto;

import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import java.util.List;

public record DashboardResponseDTO(

        Long totalPacientes,
        Long totalAvaliacoes,
        Long totalTestesIndicados,
        Long totalInconclusivos,
        List<AssessmentResponseDTO> avaliacoesRecentes

    ) {}