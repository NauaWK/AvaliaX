package dev.trabalho.xfragil.entities.dto.assessment_dtos;

import dev.trabalho.xfragil.utils.enums.Result;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AssessmentResponseDTO(

        Integer id,
        String paciente,
        String usuario,
        LocalDate dataAvaliacao,
        BigDecimal score,
        Result resultado,
        String detalhes

    ) {}