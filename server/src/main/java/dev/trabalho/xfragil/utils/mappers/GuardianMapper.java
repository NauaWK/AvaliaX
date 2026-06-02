package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianRequestDTO;
import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class GuardianMapper {

    public Guardian toGuardian(GuardianRequestDTO dto) {

        return new Guardian(
                dto.nome(),
                dto.CPF_responsavel(),
                dto.grauParentesco(),
                dto.cidade(),
                dto.estado(),
                dto.pais(),
                dto.whatsapp(),
                dto.telefone1(),
                dto.telefone2(),
                dto.email()
        );
    }

    public GuardianResponseDTO toDto(Guardian guardian) {

        return new GuardianResponseDTO(
                guardian.getId(),
                guardian.getName(),
                guardian.getCPF(),
                guardian.getRelationshipDegree(),
                guardian.getCity(),
                guardian.getState(),
                guardian.getCountry(),
                guardian.getWhatsapp(),
                guardian.getTelefone1(),
                guardian.getTelefone2(),
                guardian.getEmail()
        );
    }
    
}
