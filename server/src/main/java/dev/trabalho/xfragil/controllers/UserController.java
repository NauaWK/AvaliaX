
package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.user_dtos.UserRequestDTO;
import dev.trabalho.xfragil.entities.dto.user_dtos.UserResponseDTO;
import dev.trabalho.xfragil.services.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers()
    {
        List<UserResponseDTO> dtos = userService.getAllUsers();
        return ResponseEntity.ok(dtos);
    }
       
    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Valid UserRequestDTO userRequest)
    {
        UserResponseDTO dto = userService.addUser(userRequest);                
        return ResponseEntity.created(URI.create("/usuarios/" + dto.id())).body(dto);
        
    }
    
    
}
