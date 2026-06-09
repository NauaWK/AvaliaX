
package dev.trabalho.xfragil.entities.dto.symptom_dto;

import jakarta.validation.constraints.NotBlank;

public record SymptomRequestDTO(
        
        @NotBlank
        String nome,

        boolean presente
        
        ) {}