package dev.trabalho.xfragil.entities.dto.guardian_dtos;

public record GuardianResponseDTO(

        Integer id,
        String nome,
        String CPF,
        String grauParentesco,
        String cidade,
        String estado,
        String pais,
        String whatsapp,
        String telefone1,
        String telefone2,
        String email

    ) {}
