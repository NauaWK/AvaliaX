package dev.trabalho.xfragil.entities.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDTO(

        @NotBlank(message = "O campo de login é obrigatório!")
        @Size(max = 50, message = "O login não deve ultrapassar 50 caracteres!")
        String login,

        @NotBlank(message = "A senha é obrigatória!")
        @Size(max = 255, message = "A senha não deve ultrapassar 255 caracteres!")
        String senha
        
        ) {}

