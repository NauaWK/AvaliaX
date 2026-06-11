
package dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos;


public record PatientResponseEditDTO(

        Integer id,
        
        String nome,
        
        String genero,
        
        Integer idade,
        
        String nomeMae,
        
        String nomePai,
        
        Boolean ativo

    ) {}


