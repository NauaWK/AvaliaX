
package dev.trabalho.xfragil.entities.dto.patient_dtos;

import dev.trabalho.xfragil.utils.enums.Answer;
import dev.trabalho.xfragil.utils.enums.ExamResult;
import java.time.LocalDate;

public record PatientGeneralDataDTO(
        
        String nome,
        
        String genero,
        
        LocalDate dataNascimento,
        
        Integer idade,
        
        String nomeMae,
        
        String nomePai,
        
        String responsavel,
        
        String telefone,
        
        String email,
        
        Answer testeDna,

        Answer interesseExame,

        ExamResult resultadoExame,

        Answer diagnosticoAutismo,

        Answer possuiIrmaos,

        Answer antecedentesDeficiencia,

        Answer antecedentesMenopausa,

        Answer antecedentesAtaxia
        
    ) {}
