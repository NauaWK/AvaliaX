package dev.trabalho.xfragil.entities.dto.response_dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecentAssessmentResponseDTO(

        Integer idAvaliacao,
        Integer idPaciente,
        String nomePaciente,
        LocalDate dataAvaliacao,
        BigDecimal score,
        String recomendacao

) {}