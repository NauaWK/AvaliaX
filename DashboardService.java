package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.dto.response_dto.DashboardResponseDTO;
import dev.trabalho.xfragil.entities.dto.response_dto.RecentAssessmentResponseDTO;
import dev.trabalho.xfragil.repositories.DashboardRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepo;

    public DashboardService(DashboardRepository dashboardRepo) {
        this.dashboardRepo = dashboardRepo;
    }

    public DashboardResponseDTO getDashboardData() {

        Long totalPacientes = dashboardRepo.countPacientes();
        Long totalAvaliacoes = dashboardRepo.countAvaliacoes();
        List<RecentAssessmentResponseDTO> avaliacoesRecentes = dashboardRepo.findRecentAssessments();

        return new DashboardResponseDTO(
                totalPacientes,
                totalAvaliacoes,
                avaliacoesRecentes
        );
    }
}