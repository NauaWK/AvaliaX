package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.dashboard_dto.DashboardResponseDTO;
import dev.trabalho.xfragil.services.DashboardService;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboardData() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) auth.getDetails();      
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        
        boolean isAdmin = roles.stream()
                           .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        
        DashboardResponseDTO dto;
        if(isAdmin) dto = dashboardService.getAdminDashboardData(userId);
        else dto = dashboardService.getDashboardData(userId);

        return ResponseEntity.ok(dto);
    }
}