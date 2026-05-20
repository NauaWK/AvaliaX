
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.request_dto.UserRequestDTO;
import dev.trabalho.xfragil.entities.dto.response_dto.UserResponseDTO;
import dev.trabalho.xfragil.exception.customExceptions.DuplicatedUserException;
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
       if(userAlreadyExists(userRequest.login())) throw new DuplicatedUserException("Este usuário já existe.");
        
       Users user = userMapper.toUser(userRequest);
       user.setPassword(encoder.encode(user.getPassword()));
       
       userRepo.save(user);
       return userMapper.toDto(user);
    }
    
    public UserResponseDTO editUser(Integer id, UserRequestDTO userRequest)
    {   
        Users user = userRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário com ID " + id + " não encontrado!"));

        user.setLogin(userRequest.login());
        user.setEmail(userRequest.email());
        user.setPassword(encoder.encode(userRequest.senha()));
        user.setRole(userRequest.perfil());
        
        userRepo.save(user);
        return userMapper.toDto(user);
    }
    
    public void deleteUser(Integer id)
    {   
        userRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Usuário com ID " + id + " não encontrado!"));         
        userRepo.deleteById(id);   
    }
    
    
    private boolean userAlreadyExists(String login){
        return userRepo.existsByLogin(login);
    }
    
}
