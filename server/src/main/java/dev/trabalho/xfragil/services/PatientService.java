
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.guardian_dtos.GuardianResponseDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos.PatientRequestEditDTOUser;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos.PatientRequestEditDTOAdmin;
import dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos.PatientResponseEditDTO;
import dev.trabalho.xfragil.exception.customExceptions.DuplicatedObjectException;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import dev.trabalho.xfragil.repositories.GuardianRepository;
import dev.trabalho.xfragil.repositories.PatientGuardianRepository;
import dev.trabalho.xfragil.repositories.PatientRepository;
import dev.trabalho.xfragil.utils.mappers.GuardianMapper;
import dev.trabalho.xfragil.utils.mappers.PatientMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    
    private final PatientRepository patientRepo;
    private final PatientMapper patientMapper;
    private final PatientGuardianService patientGuardianService;
    private final PatientGuardianRepository patientGuardianRepo;
    private final GuardianService guardianService;
    private final GuardianRepository guardianRepo;
    private final GuardianMapper guardianMapper;
    private final UserService userService;

    public PatientService(
            PatientRepository patientRepo, 
            PatientMapper patientMapper, 
            PatientGuardianService patientGuardianService, 
            PatientGuardianRepository patientGuardianRepo, 
            GuardianService guardianService, 
            GuardianRepository guardianRepo, 
            GuardianMapper guardianMapper, 
            UserService userService) {
        this.patientRepo = patientRepo;
        this.patientMapper = patientMapper;
        this.patientGuardianService = patientGuardianService;
        this.patientGuardianRepo = patientGuardianRepo;
        this.guardianService = guardianService;
        this.guardianRepo = guardianRepo;
        this.guardianMapper = guardianMapper;
        this.userService = userService;
    }
    
    public List<PatientResponseDTO> getPatients() 
    {
    List<Patient> patients = patientRepo.findAll();

    return patients.stream()
            .map(patient -> {
                List<GuardianResponseDTO> guardians = patientGuardianRepo.findByPatient(patient)
                        .stream()
                        .map(pg -> guardianMapper.toDto(pg.getGuardian()))
                        .distinct()
                        .toList();

                return patientMapper.toDto(patient, guardians);
            })
            .toList();
    }

    public List<PatientResponseDTO> getPatientsByUserId(Integer userId)
    {
        List<Patient> patients = patientRepo.findByUserIdAndActiveTrue(userId);
        
        return patients.stream()
            .map(patient -> {
                List<GuardianResponseDTO> guardians = patientGuardianRepo.findByPatient(patient)
                        .stream()
                        .map(pg -> guardianMapper.toDto(pg.getGuardian()))
                        .distinct()
                        .toList();

                return patientMapper.toDto(patient, guardians);
            })
            .toList();
    }
        
    public PatientResponseDTO getPatientByCPF(String cpf) 
    {
        String normalizedCpf = cpf.replaceAll("\\D", "");

        Patient p = findPatientByCPFUser(normalizedCpf);

        List<GuardianResponseDTO> guardians = patientGuardianRepo.findByPatient(p)
                .stream()
                .map(pg -> guardianMapper.toDto(pg.getGuardian()))
                .distinct()
                .toList();

        return patientMapper.toDto(p, guardians);
    }

    public PatientResponseDTO getPatientByCPFAdmin(String cpf)
    {
        String normalizedCpf = cpf.replaceAll("\\D", "");

        Patient p = findPatientByCPF(normalizedCpf);

        List<GuardianResponseDTO> guardians = patientGuardianRepo.findByPatient(p)
                .stream()
                .map(pg -> guardianMapper.toDto(pg.getGuardian()))
                .distinct()
                .toList();

        return patientMapper.toDto(p, guardians);
    }
    
    public PatientResponseDTO addPatient(PatientRequestDTO patientRequest, Integer userId)
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
        
        return patientMapper.toDto(patient, List.of(guardianMapper.toDto(guardian)));
    }
        
    public PatientResponseEditDTO editPatientAsUser(String CPF, PatientRequestEditDTOUser patientRequest)
    {
        String normalizedPatientCpf = CPF.replaceAll("\\D", "");
        Patient p = findPatientByCPFUser(normalizedPatientCpf);

        p.setName(patientRequest.nome());
        p.setCPF(normalizedPatientCpf);
        p.setGender(patientRequest.genero());
        p.setBirthDate(patientRequest.dataNascimento());
        p.setMomName(patientRequest.nomeMae());
        p.setDadName(patientRequest.nomePai());
        patientRepo.save(p);
        
        return patientMapper.toEditDto(p);
    }
    
    public PatientResponseEditDTO editPatientAsAdmin(String CPF, PatientRequestEditDTOAdmin patientRequest)
    {
        String normalizedPatientCpf = CPF.replaceAll("\\D", "");
        Patient p = findPatientByCPF(normalizedPatientCpf);
      
        p.setName(patientRequest.nome());
        p.setCPF(normalizedPatientCpf);
        p.setGender(patientRequest.genero());
        p.setBirthDate(patientRequest.dataNascimento());
        p.setMomName(patientRequest.nomeMae());
        p.setDadName(patientRequest.nomePai());
        p.setActive(patientRequest.ativo() != null ? patientRequest.ativo() : p.isActive());
        patientRepo.save(p);
        
        return patientMapper.toEditDto(p);
    }
    
    public Patient createOrFind(PatientRequestDTO dto)
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
