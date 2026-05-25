package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.entities.dto.dashboard_dto.DashboardResponseDTO;
import dev.trabalho.xfragil.repositories.AssessmentRepository;
import dev.trabalho.xfragil.repositories.PatientRepository;
import dev.trabalho.xfragil.utils.enums.Result;
import dev.trabalho.xfragil.utils.mappers.AssessmentMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final PatientRepository patientRepo;
    private final AssessmentRepository assessmentRepo;
    private final AssessmentMapper assessmentMapper;

    public DashboardService(PatientRepository patientRepo, AssessmentRepository assessmentRepo, AssessmentMapper assessmentMapper) {
        this.patientRepo = patientRepo;
        this.assessmentRepo = assessmentRepo;
        this.assessmentMapper = assessmentMapper;
    }

    public DashboardResponseDTO getDashboardData(Integer userId) 
    {
        Long totalPatients = patientRepo.countByUserId(userId);
        Long totalAssessments = assessmentRepo.countByUserId(userId);
        Long totalTestesIndicados = assessmentRepo.countByUserIdAndResult(userId, Result.TESTE_INDICADO);
        Long totalInconclusivos = assessmentRepo.countByUserIdAndResult(userId, Result.INCONCLUSIVO);
        List<Assessment> recentAssessments = assessmentRepo.findRecentAssessmentsByUserId(userId);

        List<AssessmentResponseDTO> assessmentDtos = recentAssessments
                .stream()
                .map(assessmentMapper::toDto)
                .toList();

        return new DashboardResponseDTO(
                totalPatients,
                totalAssessments,
                totalTestesIndicados,
                totalInconclusivos,
                assessmentDtos
        );
    }
    
    public DashboardResponseDTO getAdminDashboardData(Integer userId)
    {
        Long totalPatients = patientRepo.count();
        Long totalAssessments = assessmentRepo.count();
        Long totalTestesIndicados = assessmentRepo.countByResult(Result.TESTE_INDICADO);
        Long totalInconclusivos = assessmentRepo.countByResult(Result.INCONCLUSIVO);
        List<Assessment> recentAssessments = assessmentRepo.findRecentAssessments();

        List<AssessmentResponseDTO> assessmentDtos = recentAssessments
                .stream()
                .map(assessmentMapper::toDto)
                .toList();

        return new DashboardResponseDTO(
                totalPatients,
                totalAssessments,
                totalTestesIndicados,
                totalInconclusivos,
                assessmentDtos
        );
    }
    
}