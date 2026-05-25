
package dev.trabalho.xfragil.entities.dto.patient_dtos;

public record PatientResponseDTO(
        
        Integer id,
        String nome,
        String genero,
        Integer idade,
        String guardiao
        
        ) {}
