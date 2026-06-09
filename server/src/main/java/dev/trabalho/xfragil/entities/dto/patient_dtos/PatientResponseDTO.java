
package dev.trabalho.xfragil.entities.dto.patient_dtos;

import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianResponseDTO;
import java.time.LocalDate;
import java.util.List;

public record PatientResponseDTO(
        
        Integer id,
        
        String nome,
        
        String genero,
        
        LocalDate dataNascimento,
        
        String nomeMae,
        
        String nomePai,
        
        Boolean ativo,
        
        List<GuardianResponseDTO> responsaveis
        
        ) {}
