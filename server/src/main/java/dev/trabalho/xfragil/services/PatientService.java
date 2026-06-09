
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTOAdmin;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTOUser;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import dev.trabalho.xfragil.exception.customExceptions.DuplicatedObjectException;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import dev.trabalho.xfragil.repositories.PatientRepository;
import dev.trabalho.xfragil.utils.mappers.PatientMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    
    private final PatientRepository patientRepo;
    private final PatientMapper patientMapper;
    private final PatientGuardianService patientGuardianService;
    private final GuardianService guardianService;
    private final UserService userService;

    public PatientService(
            PatientRepository patientRepo, 
            PatientMapper patientMapper, 
            GuardianService guardianService, 
            PatientGuardianService patientGuardianService, 
            UserService userService) {
        this.patientRepo = patientRepo;
        this.patientMapper = patientMapper;
        this.guardianService = guardianService;
        this.patientGuardianService = patientGuardianService;
        this.userService = userService;
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
        List<Patient> patients = patientRepo.findByUserIdAndActiveTrue(userId);
        
        List<PatientResponseDTO> dtos = patients.stream()
               .map(patientMapper::toDto)
               .toList();
        
        return dtos;
    }
    
    public PatientResponseDTO addPatient(PatientRequestDTOUser patientRequest, Integer userId)
    {
        String normalizedCpf = patientRequest.CPF_paciente().replaceAll("\\D", "");
        if(patientAlreadyExists(normalizedCpf) || guardianService.guardianAlreadyExists(normalizedCpf)) throw new DuplicatedObjectException("O CPF " + normalizedCpf + " já está cadastrado!");
        
        Users user = userService.findUserByUserId(userId);
        Guardian guardian = guardianService.createOrFind(patientRequest.responsavel());
                
        Patient patient = patientMapper.toPatient(patientRequest, user);
        patient.setCPF(normalizedCpf);
        patient.setActive(true);
        patientRepo.save(patient);
        
        patientGuardianService.linkPatientToGuardian(patient, guardian);
        
        return patientMapper.toDto(patient);
    }
    
    public PatientResponseDTO getPatientByCPF(String cpf)
    {
        String normalizedCpf = cpf.replaceAll("\\D", "");
        
        Patient p = patientRepo.findByCPFAndActiveTrue(normalizedCpf)
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com CPF " + cpf + " não encontrado."));
        
        return patientMapper.toDto(p);
    }
    
    public PatientResponseDTO editPatientAsAdmin(String CPF, PatientRequestDTOAdmin patientRequest)
    {

        String normalizedCpf = patientRequest.CPF_paciente().replaceAll("\\D", "");
        Patient p = findPatientByCPF(normalizedCpf);
        
        p.setName(patientRequest.nome());
        p.setCPF(normalizedCpf);
        p.setGender(patientRequest.genero());
        p.setBirthDate(patientRequest.dataNascimento());
        p.setMomName(patientRequest.nomeMae());
        p.setDadName(patientRequest.nomePai());
        p.setActive(patientRequest.ativo() != null ? patientRequest.ativo() : p.isActive());
        
        patientRepo.save(p);
        return patientMapper.toDto(p);
    }
    
    public PatientResponseDTO editPatientAsUser(String CPF, PatientRequestDTOUser patientRequest)
    {
        String normalizedCpf = patientRequest.CPF_paciente().replaceAll("\\D", "");
        Patient p = findPatientByCPFUser(normalizedCpf);
        
        p.setName(patientRequest.nome());
        p.setCPF(normalizedCpf);
        p.setGender(patientRequest.genero());
        p.setBirthDate(patientRequest.dataNascimento());
        p.setMomName(patientRequest.nomeMae());
        p.setDadName(patientRequest.nomePai());
        
        patientRepo.save(p);
        return patientMapper.toDto(p);
    }
    
    public Patient createOrFind(PatientRequestDTOUser dto)
    {
        String normalizedCpf = dto.CPF_paciente().replaceAll("\\D", "");
        Optional<Patient> optionalPatient = patientRepo.findByCPF(normalizedCpf);
        Patient p;
        if(optionalPatient.isPresent()){
            p = optionalPatient.get();
        }
        else{
           p = patientMapper.toPatient(dto, null);
           p.setCPF(normalizedCpf);
           p.setActive(true);
           patientRepo.save(p);
        }
        return p;
    }
    
    public void deletePatient(String CPF)
    {
        Patient p = findPatientByCPFUser(CPF);
        p.setActive(false);
        patientRepo.save(p);
    }
    
    public Patient findPatientById(Integer id){
        return patientRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com ID " + id + " não encontrado!"));
    }
    
    public Patient findPatientByCPFUser(String CPF){
        return patientRepo.findByCPFAndActiveTrue(CPF)
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com CPF " + CPF + " não encontrado!"));
    }
    
    public Patient findPatientByCPF(String CPF){
        return patientRepo.findByCPF(CPF)
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com CPF " + CPF + " não encontrado!"));
    }
    
    private boolean patientAlreadyExists(String CPF){
        return patientRepo.existsByCPF(CPF);
    }
    
}
