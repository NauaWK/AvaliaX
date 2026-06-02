package dev.trabalho.xfragil.entities.dto.assessment_dtos;

import dev.trabalho.xfragil.utils.enums.Result;
import dev.trabalho.xfragil.utils.enums.Origin;
import dev.trabalho.xfragil.utils.enums.Answer;
import dev.trabalho.xfragil.utils.enums.ExamResult;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AssessmentResponseDTO(

        Integer id,
        String paciente,
        String usuario,
        Origin origem,
        LocalDate dataAvaliacao,
        BigDecimal score,
        Result resultado,
        String detalhes,
        Answer testeDna,
        Answer interesseExame,
        ExamResult resultadoExame,
        Answer diagnosticoAutismo,
        Answer possuiIrmaos,
        Answer antecedentesDeficiencia,
        Answer antecedentesMenopausa,
        Answer antecedentesAtaxia

    ) {}
