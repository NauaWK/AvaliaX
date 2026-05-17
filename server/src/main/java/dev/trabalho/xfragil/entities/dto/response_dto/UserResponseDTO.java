
package dev.trabalho.xfragil.entities.dto.response_dto;

import dev.trabalho.xfragil.utils.enums.Role;

public record UserResponseDTO(
               
        Integer id,
        String login,
        String email,
        Role perfil
        
        ) {}
