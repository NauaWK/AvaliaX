
package dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos;

import java.time.LocalDate;


public record PatientResponseEditDTO(

        Integer id,
        
        String nome,
        
        String genero,
        
        LocalDate dataNascimento,
        
        Integer idade,
        
        String nomeMae,
        
        String nomePai,
        
        Boolean ativo

    ) {}


