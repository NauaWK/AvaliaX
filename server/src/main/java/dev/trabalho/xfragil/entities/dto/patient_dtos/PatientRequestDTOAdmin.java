
package dev.trabalho.xfragil.entities.dto.patient_dtos;

import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import dev.trabalho.xfragil.utils.customAnnotations.CPF_annotation.CPF;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record PatientRequestDTOAdmin(
        
        @NotBlank(message = "Nome do paciente é obrigatório!")
        @Size(max = 100, message = "O nome não pode ultrapassar 100 caracteres!")
        String nome,
        
        @CPF
        String CPF_paciente,
        
        @NotBlank(message = "Gênero do paciente é obrigatório!")
        String genero,
        
        @NotNull(message = "Data de nascimento é obrigatória!")
        @Past(message = "A data de nascimento do paciente não pode estar no futuro!")
        LocalDate dataNascimento,
        
        @NotBlank(message = "Nome da mãe é obrigatório!")
        @Size(max = 100, message = "O nome da mãe não pode ultrapassar 100 caracteres!")
        String nomeMae,
        
        @Size(max = 100, message = "O nome do pai não pode ultrapassar 100 caracteres!")
        String nomePai,
        
        Boolean ativo,
        
        @Valid
        GuardianRequestDTO responsavel
        
        ) {}
