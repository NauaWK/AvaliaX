package dev.trabalho.xfragil.controllers;

import dev.trabalho.xfragil.entities.dto.response_dto.DashboardResponseDTO;
import dev.trabalho.xfragil.services.DashboardService;
import org.springframework.http.ResponseEntity;
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

        DashboardResponseDTO dto = dashboardService.getDashboardData();

        return ResponseEntity.ok(dto);
    }
}