
package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos.PatientRequestEditDTOAdmin;
import dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos.PatientRequestEditDTOUser;
import dev.trabalho.xfragil.entities.dto.patient_dtos.edit_pacient_dtos.PatientResponseEditDTO;
import dev.trabalho.xfragil.security.UserDetailsImpl;
import dev.trabalho.xfragil.services.PatientService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.trabalho.xfragil.utils.customAnnotations.CPF_annotation.CPF;

@RestController
@RequestMapping("/api/pacientes")
@Validated
public class PatientController {
    
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(Authentication auth)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();    
        
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        boolean isAdmin = roles.stream()
                           .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        List<PatientResponseDTO> dtos = patientService.getPatients(userId, isAdmin);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Integer id)
    {
        PatientResponseDTO dto = patientService.getPatientById(id);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PatientResponseDTO> getPatientByCPF(@PathVariable @CPF String cpf)
    {
        PatientResponseDTO dto = patientService.getPatientByCPF(cpf);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/admin/{cpf}")
    public ResponseEntity<PatientResponseDTO> getPatientByCPFAdmin(@PathVariable @CPF String cpf)
    {
        PatientResponseDTO dto = patientService.getPatientByCPFAdmin(cpf);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping
    public ResponseEntity<PatientResponseDTO> addPatient(@RequestBody @Valid PatientRequestDTO patientRequest, 
            Authentication auth)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();
        PatientResponseDTO dto = patientService.addPatient(patientRequest, userId);       
        
        return ResponseEntity.created(URI.create("/api/pacientes/" + dto.id())).body(dto);
    }
    
    @PatchMapping("/admin/{cpf}")
    public ResponseEntity<PatientResponseEditDTO> editPatientAsAdmin(@PathVariable @CPF String cpf,
            @RequestBody @Valid PatientRequestEditDTOAdmin patientRequest)
    {
        PatientResponseEditDTO dto = patientService.editPatientAsAdmin(cpf, patientRequest);
        return ResponseEntity.ok(dto);
    }
    
    @PatchMapping("/{cpf}")
    public ResponseEntity<PatientResponseEditDTO> editPatientAsUser(@PathVariable @CPF String cpf,
            @RequestBody @Valid PatientRequestEditDTOUser patientRequest)
    {
        PatientResponseEditDTO dto = patientService.editPatientAsUser(cpf, patientRequest);
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletePatient(@PathVariable @CPF String cpf)
    {   
        patientService.deletePatient(cpf); 
        return ResponseEntity.noContent().build();
    }
    
}
