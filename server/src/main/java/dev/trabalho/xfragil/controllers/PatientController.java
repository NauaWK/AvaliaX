
package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientRequestDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientResponseDTO;
import dev.trabalho.xfragil.services.PatientService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pacientes")
public class PatientController {
    
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllUsers()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) auth.getDetails();      
        
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();        
        boolean isAdmin = roles.stream()
                           .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        
        
        List<PatientResponseDTO> dtos;
        if(isAdmin) dtos = patientService.getAllUsersAdmin();
        else dtos = patientService.getUsersByUserId(userId);
        
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping
    public ResponseEntity<PatientResponseDTO> addPatient(@RequestBody @Valid PatientRequestDTO patientRequest)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) auth.getDetails();              
        
        PatientResponseDTO dto = patientService.addUser(patientRequest, userId);                
        return ResponseEntity.created(URI.create("/api/pacientes/" + dto.id())).body(dto);
    
    }
       
    
}
