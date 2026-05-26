
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import dev.trabalho.xfragil.exception.customExceptions.DuplicatedObjectException;
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
    
    public List<PatientResponseDTO> getPatients()
    {
        List<Patient> patients = patientRepo.findAll();
        
        List<PatientResponseDTO> dtos = patients.stream()
               .map(patientMapper::toDto)
               .toList();
        
        return dtos;
    }
    
    public List<PatientResponseDTO> getPatientsByUserId(Integer userId)
    {
        List<Patient> patients = patientRepo.findByUserId(userId);
        
        List<PatientResponseDTO> dtos = patients.stream()
               .map(patientMapper::toDto)
               .toList();
        
        return dtos;
    }
    
    public PatientResponseDTO addUser(PatientRequestDTO patientRequest, Integer userId)
    {
        if(patientAlreadyExists(patientRequest.nome(), patientRequest.CPF())) throw new DuplicatedObjectException("Este paciente já existe.");
        
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário com ID " + userId + " não encontrado!"));
                
        Patient patient = patientMapper.toPatient(patientRequest, user);
 
        patientRepo.save(patient);
        return patientMapper.toDto(patient);
    }
    
    public PatientResponseDTO getPatientByCPF(String cpf)
    {
        String normalizedCpf = cpf.replaceAll("\\D", "");
        
        Patient p = patientRepo.findByCPF(normalizedCpf)
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com CPF " + cpf + " não encontrado."));
        
        return patientMapper.toDto(p);
    }
    
    public PatientResponseDTO editPatient(Integer id, PatientRequestDTO patientRequest)
    {
        Patient p = findPatientById(id);
        String normalizedCpf = patientRequest.CPF().replaceAll("\\D", "");
        if(patientAlreadyExists(patientRequest.nome(), normalizedCpf)) throw new DuplicatedObjectException("Este paciente já existe.");
        
        p.setName(patientRequest.nome());
        p.setCPF(normalizedCpf);
        p.setGender(patientRequest.genero());
        p.setAge(patientRequest.idade());
        p.setGuardian(patientRequest.guardiao());
        
        patientRepo.save(p);
        return patientMapper.toDto(p);
    }
    
    public void deletePatient(Integer id)
    {
        findPatientById(id);
        patientRepo.deleteById(id);
    }
    
    private Patient findPatientById(Integer id){
        return patientRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com ID " + id + " não encontrado!"));
    }
    
    private boolean patientAlreadyExists(String name, String CPF){
        return patientRepo.existsByNameOrCPF(name, CPF);
    }
    
}
