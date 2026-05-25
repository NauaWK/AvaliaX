
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import dev.trabalho.xfragil.exception.customExceptions.DuplicatedUserException;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import dev.trabalho.xfragil.repositories.PatientRepository;
import dev.trabalho.xfragil.repositories.UserRepository;
import dev.trabalho.xfragil.utils.mappers.PatientMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    
    private final PatientRepository patientRepo;
    private final PatientMapper patientMapper;
    private final UserRepository userRepo;

    public PatientService(PatientRepository patientRepo, PatientMapper patientMapper, UserRepository userRepo) {
        this.patientRepo = patientRepo;
        this.patientMapper = patientMapper;
        this.userRepo = userRepo;
    }
    
    public List<PatientResponseDTO> getAllUsersAdmin()
    {
        List<Patient> patients = patientRepo.findAll();
        
        List<PatientResponseDTO> dtos = patients.stream()
               .map(patientMapper::toDto)
               .toList();
        
        return dtos;
    }
    
    public List<PatientResponseDTO> getUsersByUserId(Integer userId)
    {
        List<Patient> patients = patientRepo.findByUserId(userId);
        
        List<PatientResponseDTO> dtos = patients.stream()
               .map(patientMapper::toDto)
               .toList();
        
        return dtos;
    }
    
    public PatientResponseDTO addUser(PatientRequestDTO patientRequest, Integer userId)
    {
        if(patientAlreadyExists(patientRequest.nome())) throw new DuplicatedUserException("Este paciente já existe.");
        
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário com ID " + userId + " não encontrado!"));
                
        Patient patient = patientMapper.toPatient(patientRequest, user);
       
        patientRepo.save(patient);
        return patientMapper.toDto(patient);
    }
    
    private boolean patientAlreadyExists(String name){
        return patientRepo.existsByName(name);
    }
    
    
    
}
