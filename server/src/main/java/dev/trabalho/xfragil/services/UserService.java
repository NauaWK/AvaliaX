
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.user_dtos.UserRequestDTO;
import dev.trabalho.xfragil.entities.dto.user_dtos.UserResponseDTO;
import dev.trabalho.xfragil.exception.customExceptions.DuplicatedObjectException;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import dev.trabalho.xfragil.repositories.UserRepository;
import dev.trabalho.xfragil.utils.mappers.UserMapper;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final PasswordEncoder encoder;
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    public UserService(PasswordEncoder encoder, UserRepository userRepo, UserMapper userMapper) 
    {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }
    
    public List<UserResponseDTO> getAllUsers()
    {
       List<Users> users = userRepo.findAll();
       
       List<UserResponseDTO> dtos = users.stream()
               .map(userMapper::toDto)
               .toList();
    
       return dtos;
    }
    
    public UserResponseDTO addUser(UserRequestDTO userRequest)
    {
       if(userAlreadyExists(userRequest.login())) throw new DuplicatedObjectException("Este login já está em uso!");
        
       Users user = userMapper.toUser(userRequest);
       user.setPassword(encoder.encode(user.getPassword()));
       user.setActive(userRequest.ativo() != null ? userRequest.ativo() : true);
       
       userRepo.save(user);
       return userMapper.toDto(user);
    }
    
    public UserResponseDTO editUser(Integer id, UserRequestDTO userRequest)
    {   
        Users user = findUserByUserId(id);

        user.setLogin(userRequest.login());
        user.setEmail(userRequest.email());
        user.setName(userRequest.nome());
        user.setPassword(encoder.encode(userRequest.senha()));
        user.setRole(userRequest.perfil());
        user.setActive(userRequest.ativo() != null ? userRequest.ativo() : user.isActive());
        
        userRepo.save(user);
        return userMapper.toDto(user);
    }
    
    public void deleteUser(Integer id)
    {   
        Users u = findUserByUserId(id);
        u.setActive(false);
        userRepo.save(u);
    }
    
    public Users findUserByUserId(Integer userId){
        return userRepo.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário com ID " + userId + " não encontrado!"));
    }
    
    private boolean userAlreadyExists(String login){
        return userRepo.existsByLogin(login);
    }
    
}
