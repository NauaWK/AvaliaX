
package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    
    public PatientResponseDTO toDto(Patient p)
    {
        return new PatientResponseDTO(
            p.getId(),
            p.getName(),
            p.getGender(),
            p.getAge(),
            p.getGuardian()
        );
    }
    
    public Patient toPatient(PatientRequestDTO dto, Users user)
    {
        return new Patient(
                dto.nome(),
                dto.CPF(),
                dto.genero(),
                dto.idade(),
                dto.guardiao(),
                user
        );
    }
    
}
