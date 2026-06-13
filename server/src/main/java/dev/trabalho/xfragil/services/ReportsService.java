package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.PatientGuardian;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentOverviewDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentStatisticsDTO;
import dev.trabalho.xfragil.entities.dto.patient_dtos.PatientGeneralDataDTO;
import dev.trabalho.xfragil.entities.dto.reports_dtos.GeneralReportUserDTO;
import dev.trabalho.xfragil.entities.dto.reports_dtos.PatientReportDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomGenderDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRankingDTO;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import dev.trabalho.xfragil.repositories.AssessmentRepository;
import dev.trabalho.xfragil.repositories.PatientGuardianRepository;
import dev.trabalho.xfragil.repositories.PatientRepository;
import dev.trabalho.xfragil.repositories.SymptomRepository;
import dev.trabalho.xfragil.utils.enums.Result;
import dev.trabalho.xfragil.utils.mappers.AssessmentMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReportsService {
    
    private final AssessmentRepository assessmentRepo;
    private final AssessmentMapper assessmentMapper;
    private final SymptomRepository symptomRepo;
    private final PatientRepository patientRepo;
    private final PatientGuardianRepository patientGuardianRepo;

    public ReportsService(
            AssessmentRepository assessmentRepo, 
            AssessmentMapper assessmentMapper, 
            SymptomRepository symptomRepo, 
            PatientRepository patientRepo, 
            PatientGuardianRepository patientGuardianRepo) {
        this.assessmentRepo = assessmentRepo;
        this.assessmentMapper = assessmentMapper;
        this.symptomRepo = symptomRepo;
        this.patientRepo = patientRepo;
        this.patientGuardianRepo = patientGuardianRepo;
    }

    //relatório para usuário comum
    public GeneralReportUserDTO getGeneralReportsUser(Integer userId) 
    {
        Double avgScore = Optional.ofNullable(assessmentRepo.findAverageScoreByUserId(userId))
                          .orElse(0.0);
        avgScore = BigDecimal.valueOf(avgScore).setScale(2, RoundingMode.HALF_UP).doubleValue();
        
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
        Double avgScore = Optional.ofNullable(assessmentRepo.findAverageScoreGlobal())
                          .orElse(0.0);
        avgScore = BigDecimal.valueOf(avgScore).setScale(2, RoundingMode.HALF_UP).doubleValue();
        
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
        indicatedTestPct = BigDecimal.valueOf(indicatedTestPct).setScale(2, RoundingMode.HALF_UP).doubleValue();
        inconclusivePct = BigDecimal.valueOf(inconclusivePct).setScale(2, RoundingMode.HALF_UP).doubleValue();
        
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

        PatientGeneralDataDTO dadosGeraisPaciente = getPatientGeneralDataDTO(patient);
        
        AssessmentStatisticsDTO estatisticasAvaliacoesPaciente = getAssessmentsStatisticsByPatient(patient);
        
        List<AssessmentOverviewDTO> top3AvaliacoesRecentes = getLastThreeAssessments(patient.getId());

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
                dadosGeraisPaciente,
                estatisticasAvaliacoesPaciente,
                top3AvaliacoesRecentes,
                top3Sintomas
        );
    }
    
    private PatientGeneralDataDTO getPatientGeneralDataDTO(Patient p) 
    {
        Assessment avaliacaoMaisRecente = assessmentRepo
            .findTopByPatientIdOrderByAssessmentDateDesc(p.getId())
            .orElse(null); 

        Integer idade = Period.between(p.getBirthDate(), LocalDate.now()).getYears();
        List<PatientGuardian> relacoes = patientGuardianRepo.findByPatient(p);
        Guardian responsavel = relacoes.isEmpty() ? null : relacoes.getFirst().getGuardian();

        return new PatientGeneralDataDTO(
            p.getName(),
            p.getGender(),
            p.getBirthDate(),
            idade,
            p.getMomName(),
            p.getDadName() != null ? p.getDadName() : "Não consta",
            responsavel != null ? responsavel.getName() : "Não consta",
            responsavel != null ? responsavel.getTelefone1() : null,
            responsavel != null ? responsavel.getEmail() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getDnaTest() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getExamInterest() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getExamResult() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getAutismDiagnosis() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getHasSiblings() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getDisabilityHistory() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getMenopauseHistory() : null,
            avaliacaoMaisRecente != null ? avaliacaoMaisRecente.getAtaxiaHistory() : null
        );
    }

    private AssessmentStatisticsDTO getAssessmentsStatisticsByPatient(Patient p)
    {
        Long totalAvaliacoes = assessmentRepo.countByPatient(p.getId());
        if (totalAvaliacoes == 0) {
            return null; 
        }
        
        Long avaliacoesProfissional = assessmentRepo.countProfissionalAssessmentByPatient(p.getId());
        Long avaliacoesResponsavel = assessmentRepo.countGuardianAssessmentsByPatient(p.getId());
        Long avaliacoesTesteIndicado = assessmentRepo.countByPatientIdAndResult(p.getId(), Result.TESTE_INDICADO);
        Long avaliacoesInconclusivas = assessmentRepo.countByPatientIdAndResult(p.getId(), Result.INCONCLUSIVO);
        
        Double mediaScore = Optional.ofNullable(assessmentRepo.findAvgScoreByPatient(p.getId()))
                          .orElse(0.0);
        mediaScore = BigDecimal.valueOf(mediaScore).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return new AssessmentStatisticsDTO(
                totalAvaliacoes,
                avaliacoesProfissional,
                avaliacoesResponsavel,
                avaliacoesTesteIndicado,
                avaliacoesInconclusivas,
                mediaScore
        );
    }
    
    private List<AssessmentOverviewDTO> getLastThreeAssessments(Integer patientId) 
    {
        List<Assessment> assessments = assessmentRepo.findTop3ByPatientIdOrderByAssessmentDateDesc(patientId);

        if (assessments.isEmpty()) {
            return List.of(); 
        }

        return assessments.stream()
            .map(a -> {
                List<String> symptoms = symptomRepo.findSymptomsByAssessment(a.getId());
                return assessmentMapper.toOverviewDto(a, symptoms);
            })
            .toList();
    }
    
}
    
