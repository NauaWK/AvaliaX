package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.reports_dtos.GeneralReportUserDTO;
import dev.trabalho.xfragil.entities.dto.reports_dtos.PatientReportDTO;
import dev.trabalho.xfragil.security.UserDetailsImpl;
import dev.trabalho.xfragil.services.ReportsService;
import dev.trabalho.xfragil.utils.customAnnotations.CPF_annotation.CPF;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
public class ReportsController {
    
    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }
    
    @GetMapping
    public ResponseEntity<GeneralReportUserDTO> getGeneralReports(Authentication auth)
    {   
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Integer userId = userDetails.getUser().getId();  
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        
        boolean isAdmin = roles.stream()
                           .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        
        GeneralReportUserDTO dto = isAdmin 
                ? reportsService.getGeneralReportsAdmin()
                : reportsService.getGeneralReportsUser(userId);
        
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<PatientReportDTO> getPatientReport(@PathVariable @CPF String cpf)
    {
        PatientReportDTO dto = reportsService.getPatientReport(cpf);
        return ResponseEntity.ok(dto);
    }
    
}