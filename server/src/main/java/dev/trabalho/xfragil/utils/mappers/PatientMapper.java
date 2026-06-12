
package dev.trabalho.xfragil.utils.mappers;

import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianResponseDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos.PatientResponseEditDTO;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    
    public PatientResponseDTO toDto(Patient p, List<GuardianResponseDTO> guardianDto)
    {
        return new PatientResponseDTO(
            p.getId(),
            p.getName(),
            p.getGender(),
            p.getBirthDate(),
            Period.between(p.getBirthDate(), LocalDate.now()).getYears(),
            p.getMomName(),
            p.getDadName(),
            p.isActive(),
            guardianDto    
        );
    }
    
    public PatientResponseEditDTO toEditDto(Patient p)
    {
        return new PatientResponseEditDTO(
            p.getId(),
            p.getName(),
            p.getGender(),
            p.getBirthDate(),
            Period.between(p.getBirthDate(), LocalDate.now()).getYears(),
            p.getMomName(),
            p.getDadName(),
            p.isActive()
        );
    }
    
    public Patient toPatient(PatientRequestDTO dto, Users user)
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
