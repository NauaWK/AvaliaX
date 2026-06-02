package dev.trabalho.xfragil.entities.dto.guardian_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import dev.trabalho.xfragil.utils.customAnnotations.CPF_annotation.CPF;
import jakarta.validation.constraints.Email;

public record GuardianRequestDTO(

        @NotBlank(message = "Nome do responsável é obrigatório!")
        @Size(max = 100, message = "O nome não pode ultrapassar 100 caracteres!")
        String nome,

        @CPF
        String CPF_responsavel,

        @NotBlank(message = "Grau de parentesco é obrigatório!")
        @Size(max = 50, message = "O grau de parentesco não pode ultrapassar 50 caracteres!")
        String grauParentesco,

        @NotBlank(message = "Cidade é obrigatória!")
        @Size(max = 100, message = "A cidade não pode ultrapassar 100 caracteres!")
        String cidade,

        @NotBlank(message = "Estado é obrigatório!")
        @Size(max = 100, message = "O estado não pode ultrapassar 100 caracteres!")
        String estado,

        @NotBlank(message = "País é obrigatório!")
        @Size(max = 100, message = "O país não pode ultrapassar 100 caracteres!")
        String pais,

        @Size(max = 20, message = "WhatsApp não pode ultrapassar 20 caracteres!")
        String whatsapp,

        @NotBlank(message = "Telefone principal é obrigatório!")
        @Size(max = 20, message = "Telefone principal não pode ultrapassar 20 caracteres!")
        String telefone1,

        @Size(max = 20, message = "Telefone secundário não pode ultrapassar 20 caracteres!")
        String telefone2,

        @NotBlank(message = "Email é obrigatório!")
        @Email(message = "Email inválido!")
        @Size(max = 255, message = "Email não pode ultrapassar 255 caracteres!")
        String email

    ) {}
