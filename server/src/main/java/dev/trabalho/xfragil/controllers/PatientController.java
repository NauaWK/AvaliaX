
package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
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
        
        
        List<PatientResponseDTO> dtos = isAdmin 
                ? patientService.getPatients() 
                : patientService.getPatientsByUserId(userId);
        
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{cpf}")
    public ResponseEntity<PatientResponseDTO> getPatientByCPF(@PathVariable @CPF String cpf)
    {
        PatientResponseDTO dto = patientService.getPatientByCPF(cpf);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping
    public ResponseEntity<PatientResponseDTO> addPatient(@RequestBody @Valid PatientRequestDTO patientRequest, 
            Authentication auth)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();
        PatientResponseDTO dto = patientService.addUser(patientRequest, userId);       
        
        return ResponseEntity.created(URI.create("/api/pacientes/" + dto.id())).body(dto);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> editPatient(@PathVariable Integer id, 
            @RequestBody @Valid PatientRequestDTO patientRequest)
    {
        PatientResponseDTO dto = patientService.editPatient(id, patientRequest); 
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id)
    {   
        patientService.deletePatient(id); 
        return ResponseEntity.noContent().build();
    }
    
}
