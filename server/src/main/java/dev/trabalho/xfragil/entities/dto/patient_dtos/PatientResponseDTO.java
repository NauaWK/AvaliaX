
package dev.trabalho.xfragil.entities.dto.patient_dtos;

import java.time.LocalDate;

public record PatientResponseDTO(
        
        Integer id,
        String nome,
        String genero,
        LocalDate dataNascimento,
        String nomeMae,
        String nomePai,
        Boolean ativo
        
        ) {}
