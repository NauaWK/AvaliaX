package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.dto.reports_dtos.GeneralReportUserDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomGenderDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRankingDTO;
import dev.trabalho.xfragil.repositories.AssessmentRepository;
import dev.trabalho.xfragil.repositories.PatientRepository;
import dev.trabalho.xfragil.repositories.SymptomRepository;
import dev.trabalho.xfragil.utils.enums.Result;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportsService {
    
    private final AssessmentRepository assessmentRepository;
    private final SymptomRepository symptomRepository;
    private final PatientRepository patientRepository;

    public ReportsService(AssessmentRepository assessmentRepository,
                          SymptomRepository symptomRepository,
                          PatientRepository patientRepository) {
        this.assessmentRepository = assessmentRepository;
        this.symptomRepository = symptomRepository;
        this.patientRepository = patientRepository;
    }

    //relatório para usuário comum
    public GeneralReportUserDTO getGeneralReportsUser(Integer userId) 
    {
        return buildReport(
            getSymptomRanking(userId),
            findMostMarkedSymptomByGender(userId),
            assessmentRepository.findAverageScoreByUserId(userId),
            assessmentRepository.countByUserId(userId),
            assessmentRepository.countByUserIdAndResult(userId, Result.TESTE_INDICADO),
            assessmentRepository.countByUserIdAndResult(userId, Result.INCONCLUSIVO)
        );
    }

    //relatório para admin (sem filtro de userId)
    public GeneralReportUserDTO getGeneralReportsAdmin() 
    {
        return buildReport(
            getSymptomRanking(null), // versão global
            findMostMarkedSymptomByGender(null),
            assessmentRepository.findAverageScoreGlobal(),
            assessmentRepository.count(),
            assessmentRepository.countByResult(Result.TESTE_INDICADO),
            assessmentRepository.countByResult(Result.INCONCLUSIVO)
        );
    }

    private GeneralReportUserDTO buildReport(
            List<SymptomRankingDTO> ranking,
            SymptomGenderDTO symptomGender,
            Double avgScore,
            Long totalAssessments,
            Long indicatedTest,
            Long inconclusive) 
    {
        Double indicatedTestPct = totalAssessments > 0 ? (indicatedTest * 100.0 / totalAssessments) : 0.0;
        Double inconclusivePct = totalAssessments > 0 ? (inconclusive * 100.0 / totalAssessments) : 0.0;

        return new GeneralReportUserDTO(ranking, symptomGender, avgScore, indicatedTestPct, inconclusivePct);
    }

    private List<SymptomRankingDTO> getSymptomRanking(Integer userId) 
    {
        List<Object[]> result = (userId != null)
                ? symptomRepository.findSymptomRanking(userId)
                : symptomRepository.findSymptomRankingGlobal();

        return result.stream()
                .map(r -> new SymptomRankingDTO((String) r[0], ((Number) r[1]).longValue()))
                .toList();
    }

    private SymptomGenderDTO findMostMarkedSymptomByGender(Integer userId) 
    {
        Object[] menResult = (userId != null)
                ? symptomRepository.findTopSymptomByGender(userId, "M")
                : symptomRepository.findTopSymptomByGenderGlobal("M");

        Object[] womenResult = (userId != null)
                ? symptomRepository.findTopSymptomByGender(userId, "F")
                : symptomRepository.findTopSymptomByGenderGlobal("F");

        String menSymptom = menResult != null ? (String) menResult[0] : null;
        Long menQuantity = menResult != null ? ((Number) menResult[1]).longValue() : 0L;

        String womenSymptom = womenResult != null ? (String) womenResult[0] : null;
        Long womenQuantity = womenResult != null ? ((Number) womenResult[1]).longValue() : 0L;

        return new SymptomGenderDTO(menSymptom, menQuantity, womenSymptom, womenQuantity);
    }
    
}
    
