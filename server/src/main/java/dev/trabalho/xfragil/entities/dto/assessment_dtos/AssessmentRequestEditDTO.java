package dev.trabalho.xfragil.entities.dto.assessment_dtos;

import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRequestDTO;
import dev.trabalho.xfragil.utils.enums.Answer;
import dev.trabalho.xfragil.utils.enums.ExamResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record AssessmentRequestEditDTO (

        @Size(max = 255, message = "Detalhes não podem passar de 255 caracteres!")
        String detalhes,

        @NotNull(message = "Campo exame de DNA é obrigatório")
        Answer testeDna,

        Answer interesseExame,

        ExamResult resultadoExame,

        @NotNull(message = "Diagnóstico de autismo é obrigatório")
        Answer diagnosticoAutismo,

        @NotNull(message = "Campo possui irmãos é obrigatório")
        Answer possuiIrmaos,

        @NotNull(message = "Antecedentes de deficiência são obrigatórios")
        Answer antecedentesDeficiencia,

        @NotNull(message = "Antecedentes de menopausa são obrigatórios")
        Answer antecedentesMenopausa,

        @NotNull(message = "Antecedentes de ataxia são obrigatórios")
        Answer antecedentesAtaxia,

        @Valid
        List<SymptomRequestDTO> sintomas

    ) {}

