
package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTOUser;
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
            p.getBirthDate(),
            p.getMomName(),
            p.getDadName(),
            p.isActive()
        );
    }
    
    public Patient toPatient(PatientRequestDTOUser dto, Users user)
    {
        return new Patient(
                dto.nome(),
                dto.CPF_paciente(),
                dto.genero(),
                dto.dataNascimento(),
                dto.nomeMae(),
                dto.nomePai(),
                user
        );
    }
    
}
