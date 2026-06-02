package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.auth_dtos.AuthRequestDTO;
import dev.trabalho.xfragil.entities.dto.auth_dtos.AuthResponseDTO;
import dev.trabalho.xfragil.exception.customExceptions.InactiveUserException;
import dev.trabalho.xfragil.exception.customExceptions.InvalidCredentialsException;
import dev.trabalho.xfragil.security.UserDetailsImpl;
import dev.trabalho.xfragil.security.jwt.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO authRequestDTO){

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.login(), authRequestDTO.senha())
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponseDTO(jwt));
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidCredentialsException("Credenciais inválidas!");
        }

    }

}
