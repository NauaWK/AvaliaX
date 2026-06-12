package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.dto.reports_dtos.GeneralReportUserDTO;
import dev.trabalho.xfragil.entities.dto.reports_dtos.PatientReportDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomGenderDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRankingDTO;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import dev.trabalho.xfragil.repositories.AssessmentRepository;
import dev.trabalho.xfragil.repositories.PatientRepository;
import dev.trabalho.xfragil.repositories.SymptomRepository;
import dev.trabalho.xfragil.utils.enums.Result;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReportsService {
    
    private final AssessmentRepository assessmentRepo;
    private final SymptomRepository symptomRepo;
    private final PatientRepository patientRepo;

    public ReportsService(
            AssessmentRepository assessmentRepo, 
            SymptomRepository symptomRepo, 
            PatientRepository patientRepo) {
        this.assessmentRepo = assessmentRepo;
        this.symptomRepo = symptomRepo;
        this.patientRepo = patientRepo;
    }

    //relatório para usuário comum
    public GeneralReportUserDTO getGeneralReportsUser(Integer userId) 
    {
        Double avgScore = Optional.ofNullable(
        assessmentRepo.findAverageScoreByUserId(userId)
        ).orElse(0.0);
        
        return buildReport(
            getSymptomRanking(userId),
            findMostMarkedSymptomByGender(userId),
            avgScore,
            assessmentRepo.countByUserId(userId),
            assessmentRepo.countByUserIdAndResult(userId, Result.TESTE_INDICADO),
            assessmentRepo.countByUserIdAndResult(userId, Result.INCONCLUSIVO)
        );
    }

    //relatório para admin (sem filtro de userId)
    public GeneralReportUserDTO getGeneralReportsAdmin() 
    {
        Double avgScore = Optional.ofNullable(
        assessmentRepo.findAverageScoreGlobal()
        ).orElse(0.0);
        
        return buildReport(
            getSymptomRanking(null),
            findMostMarkedSymptomByGender(null),
            avgScore,
            assessmentRepo.count(),
            assessmentRepo.countByResult(Result.TESTE_INDICADO),
            assessmentRepo.countByResult(Result.INCONCLUSIVO)
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
        return new GeneralReportUserDTO(ranking, symptomGender, totalAssessments, indicatedTestPct, inconclusivePct, avgScore);
    }

    private List<SymptomRankingDTO> getSymptomRanking(Integer userId) 
    {
    List<Object[]> result = (userId != null)
            ? symptomRepo.findSymptomRanking(userId)
            : symptomRepo.findSymptomRankingGlobal();

    return result.stream()
            .filter(r -> r != null && r.length >= 2) //garante que tem pelo menos 2 colunas
            .map(r -> new SymptomRankingDTO(
                    (String) r[0],
                    ((Number) r[1]).longValue()
            ))
            .toList();
    }

    private SymptomGenderDTO findMostMarkedSymptomByGender(Integer userId) 
    {
        List<Object[]> menList = (userId != null)
                ? symptomRepo.findTopSymptomByGender(userId, "M")
                : symptomRepo.findTopSymptomByGenderGlobal("M");

        List<Object[]> womenList = (userId != null)
                ? symptomRepo.findTopSymptomByGender(userId, "F")
                : symptomRepo.findTopSymptomByGenderGlobal("F");

        Object[] menResult = !menList.isEmpty() ? menList.get(0) : null;
        Object[] womenResult = !womenList.isEmpty() ? womenList.get(0) : null;

        String menSymptom = (menResult != null && menResult.length > 0) ? menResult[0].toString() : null;
        Long menQuantity = (menResult != null && menResult.length > 1 && menResult[1] instanceof Number)
                ? ((Number) menResult[1]).longValue()
                : 0L;

        String womenSymptom = (womenResult != null && womenResult.length > 0) ? womenResult[0].toString() : null;
        Long womenQuantity = (womenResult != null && womenResult.length > 1 && womenResult[1] instanceof Number)
                ? ((Number) womenResult[1]).longValue()
                : 0L;

        return new SymptomGenderDTO(menSymptom, menQuantity, womenSymptom, womenQuantity);
    }

    public PatientReportDTO getPatientReport(String cpf) 
    {
        String normalizedCpf = cpf.replaceAll("\\D", "");

        Patient patient = patientRepo.findByCPFAndActiveTrue(normalizedCpf)
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com CPF " + cpf + " não encontrado!"));

        Integer idade = Period.between(patient.getBirthDate(), LocalDate.now()).getYears();

        Long totalAvaliacoes = assessmentRepo.countByPatient(patient.getId());
        Long avaliacoesProfissional = assessmentRepo.countProfissionalAssessmentByPatient(patient.getId());
        Long avaliacoesResponsavel = assessmentRepo.countGuardianAssessmentsByPatient(patient.getId());

        Double mediaScore = Optional.ofNullable(
            assessmentRepo.findAvgScoreByPatient(patient.getId())
        ).orElse(0.0);

        List<SymptomRankingDTO> top3Sintomas = assessmentRepo
            .findSymptomRankingByPatient(patient.getId())
            .stream()
            .limit(3)
            .map(obj -> {
                String sintoma = (obj[0] != null) ? obj[0].toString() : "Desconhecido";
                Long qtd = (obj[1] instanceof Number) ? ((Number) obj[1]).longValue() : 0L;
                return new SymptomRankingDTO(sintoma, qtd);
            })
            .toList();

        return new PatientReportDTO(
                patient.getName(),
                idade,
                patient.getGender(),
                patient.getMomName(),
                patient.getDadName(),
                totalAvaliacoes,
                avaliacoesProfissional,
                avaliacoesResponsavel,
                mediaScore,
                top3Sintomas
        );
    }
    
}
    
