
package dev.trabalho.xfragil.entities.dto.request_dto;

import dev.trabalho.xfragil.utils.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        
        @NotBlank(message = "Campo de login é obrigatório!")
        @Size(max = 50, message = "O login não deve ultrapassar 50 caracteres!")
        String login, 
        
        @Email(message = "Formato de email inválido!")
        @Size(max = 255, message = "O email não deve ultrapassar 255 caracteres!")
        String email, 
                
        @NotBlank(message = "A senha é obrigatória!")
        @Size(max = 255, message = "A senha não deve ultrapassar 255 caracteres!")
        String senha,
                
        @NotNull(message = "Perfil do usuário é obrigatório!")
        Role perfil
        
        ) {}
