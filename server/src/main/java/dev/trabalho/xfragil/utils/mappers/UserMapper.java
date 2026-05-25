
package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.user_dtos.UserRequestDTO;
import dev.trabalho.xfragil.entities.dto.user_dtos.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  
    public Users toUser(UserRequestDTO dto)
    {
        return new Users(
            dto.login(),
            dto.nome(),
            dto.email(),
            dto.senha(),
            dto.perfil()
        );
    }
    
    public UserResponseDTO toDto(Users user)
    {
        return new UserResponseDTO(
            user.getId(),
            user.getLogin(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }
    
}
