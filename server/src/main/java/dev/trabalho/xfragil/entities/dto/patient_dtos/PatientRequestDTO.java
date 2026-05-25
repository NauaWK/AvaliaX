
package dev.trabalho.xfragil.entities.dto.patient_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record PatientRequestDTO(
        
        @NotBlank(message = "Nome do paciente é obrigatório!")
        @Size(max = 100, message = "O nome não pode ultrapassar 100 caracteres!")
        String nome,
        
        @NotBlank(message = "CPF do paciente é obrigatório!")
        @Size(min = 11, message = "O CPF precisa ter 11 digitos!")
        String CPF,
        
        @NotBlank(message = "Gênero do paciente é obrigatório!")
        String genero,
        
        @NotNull(message = "Idade do paciente é obrigatória!")
        @Range(min = 1, message = "Idade precisa ser maior do que 1!")
        Integer idade,
        
        @NotBlank(message = "Nome do guardião é obrigatório!")
        @Size(max = 100, message = "O nome do guardião não pode ultrapassar 100 caracteres!")
        String guardiao
        
        ) {}
