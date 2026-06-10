
package dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record PatientRequestEditDTOAdmin(

        @NotBlank String nome,

        @NotBlank String genero,

        @NotNull @Past LocalDate dataNascimento,

        @NotBlank String nomeMae,

        String nomePai,
        
        Boolean ativo

    ) {}
