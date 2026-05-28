
package dev.trabalho.xfragil.entities.dto.user_dtos;

import dev.trabalho.xfragil.utils.enums.Role;

public record UserResponseDTO(
               
        Integer id,
        String login,
        String nome,
        String email,
        Role perfil
        
        ) {}
