
package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestEditDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.entities.dto.autoassessment_dtos.AutoAssessmentRequestDTO;
import dev.trabalho.xfragil.security.UserDetailsImpl;
import dev.trabalho.xfragil.services.AssessmentService;
import dev.trabalho.xfragil.services.AutoAssessmentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collection;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


@RestController
@RequestMapping("/api/avaliacoes")
public class AssessmentController {
    
    private final AssessmentService assessmentService;
    private final AutoAssessmentService autoAssessmentService;

    public AssessmentController(AssessmentService assessmentService, AutoAssessmentService autoAssessmentService) {
        this.assessmentService = assessmentService;
        this.autoAssessmentService = autoAssessmentService;
    }
    
    @GetMapping
    public ResponseEntity<List<AssessmentResponseDTO>> getAssessments(Authentication auth)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();    
        
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();        
        boolean isAdmin = roles.stream()
                           .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        
        return ResponseEntity.ok(assessmentService.getAllAssessments(userId, isAdmin));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponseDTO> getAssessmentById(Authentication auth, @PathVariable Integer id)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();    
        
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();        
        boolean isAdmin = roles.stream()
                           .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        
        return ResponseEntity.ok(assessmentService.getAssessmentById(id, userId, isAdmin));
    }
    
    @PostMapping
    public ResponseEntity<AssessmentResponseDTO> addAssessment(@RequestBody @Valid AssessmentRequestDTO assessmentRequest, 
            Authentication auth)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();
        AssessmentResponseDTO dto = assessmentService.addAssessment(assessmentRequest, userId);       
        return ResponseEntity.created(URI.create("/api/avaliacoes/" + dto.id())).body(dto);
    }
    
    @PostMapping("/autoavaliacao")
    public ResponseEntity<AssessmentResponseDTO> addAutoAssessment(
            @Valid @RequestBody AutoAssessmentRequestDTO autoAssessmentRequest) {
        
        AssessmentResponseDTO dto = autoAssessmentService.processAutoAssessment(autoAssessmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AssessmentResponseDTO> updateAssessment(
            @PathVariable Integer id,
            @Valid @RequestBody AssessmentRequestEditDTO request,
            Authentication auth)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();

        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        boolean isAdmin = roles.stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        AssessmentResponseDTO dto = assessmentService.updateAssessment(id, request, userId, isAdmin);
        return ResponseEntity.ok(dto);
    }

}
