
package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.security.UserDetailsImpl;
import dev.trabalho.xfragil.services.AssessmentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/avaliacoes")
public class AssessmentController {
    
    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }
    
    @GetMapping
    public ResponseEntity<List<AssessmentResponseDTO>> getAssessments(Authentication auth)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();    
        
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();        
        boolean isAdmin = roles.stream()
                           .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        
        
        List<AssessmentResponseDTO> dtos = isAdmin 
                ? assessmentService.getAssessments() 
                : assessmentService.getAssessmentsByUserId(userId);
        
        return ResponseEntity.ok(dtos);
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
    
}
