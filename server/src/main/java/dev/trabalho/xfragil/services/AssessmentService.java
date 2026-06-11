
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.AssessmentSymptom;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Symptom;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestEditDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRequestDTO;
import dev.trabalho.xfragil.exception.customExceptions.ObjectNotFoundException;
import dev.trabalho.xfragil.repositories.AssessmentRepository;
import dev.trabalho.xfragil.repositories.AssessmentSymptomRepository;
import dev.trabalho.xfragil.repositories.SymptomRepository;
import dev.trabalho.xfragil.utils.enums.Origin;
import dev.trabalho.xfragil.utils.enums.Result;
import dev.trabalho.xfragil.utils.mappers.AssessmentMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {
    
    private final AssessmentRepository assessmentRepo;
    private final AssessmentMapper assessmentMapper;
    private final PatientService patientService;
    private final UserService userService;
    private final SymptomRepository symptomRepo;
    private final AssessmentSymptomRepository assessmentSymptomRepo;

    private final BigDecimal WOMAN_LIMIAR = new BigDecimal("0.55");
    private final BigDecimal MAN_LIMIAR = new BigDecimal("0.56");

    public AssessmentService(
            AssessmentRepository assessmentRepo, 
            AssessmentMapper assessmentMapper, 
            PatientService patientService, 
            UserService userService, SymptomRepository symptomRepo, 
            AssessmentSymptomRepository assessmentSymptomRepo) {
        this.assessmentRepo = assessmentRepo;
        this.assessmentMapper = assessmentMapper;
        this.patientService = patientService;
        this.userService = userService;
        this.symptomRepo = symptomRepo;
        this.assessmentSymptomRepo = assessmentSymptomRepo;
    }
    
    public List<AssessmentResponseDTO> getAssessments()
    {
        List<Assessment> assessments = assessmentRepo.findAll();

        return assessments.stream()
               .map( a -> {
                   List<String> symptoms = symptomRepo.findSymptomsByAssessment(a.getId());
                   return assessmentMapper.toDto(a, symptoms);
               })
               .toList();
    }
    
    public List<AssessmentResponseDTO> getAssessmentsByUserId(Integer userId)
    {
        List<Assessment> assessments = assessmentRepo.findByUserId(userId);

        return assessments.stream()
                .map( a -> {
                    List<String> symptoms = symptomRepo.findSymptomsByAssessment(a.getId());
                    return assessmentMapper.toDto(a, symptoms);
                })
                .toList();
    }
    
    public AssessmentResponseDTO addAssessment(AssessmentRequestDTO assessmentRequest, Integer userId) 
    {
        String normalizedCpf = assessmentRequest.CPF_paciente().replaceAll("\\D", "");
        Patient patient = patientService.findPatientByCPFUser(normalizedCpf);
        Users user = userService.findUserByUserId(userId);
        Origin origin = user != null ? Origin.PROFISSIONAL : Origin.RESPONSAVEL;

        return createAssessment(patient, user, origin, assessmentRequest);
    }

    public AssessmentResponseDTO addAutoAssessment(Patient patient, AssessmentRequestDTO assessmentRequest) {
        return createAssessment(patient, null, Origin.RESPONSAVEL, assessmentRequest);
    }

    private AssessmentResponseDTO createAssessment(Patient patient,
                                                   Users user,
                                                   Origin origin,
                                                   AssessmentRequestDTO assessmentRequest) 
    {
        boolean isMan = patient.getGender().equalsIgnoreCase("M");

        Map<String, Symptom> symptomMap = loadSymptomsByName(assessmentRequest.sintomas());
        BigDecimal score = calculateScore(assessmentRequest, isMan, symptomMap);

        Result result = (isMan && score.compareTo(MAN_LIMIAR) >= 0) ||
                        (!isMan && score.compareTo(WOMAN_LIMIAR) >= 0)
                        ? Result.TESTE_INDICADO : Result.INCONCLUSIVO;

        Assessment assessment = assessmentMapper.toAssessment(patient, user, origin, score, result, assessmentRequest);
        assessmentRepo.save(assessment);

        persistSymptoms(assessment, assessmentRequest.sintomas(), symptomMap);

        List<String> symptoms = symptomRepo.findSymptomsByAssessment(assessment.getId());
        return assessmentMapper.toDto(assessment, symptoms);
    }

    public AssessmentResponseDTO updateAssessment(Integer assessmentId,
                                                  AssessmentRequestEditDTO assessmentRequest,
                                                  Integer userId,
                                                  boolean isAdmin) {

        Assessment assessment = isAdmin
                ? assessmentRepo.findById(assessmentId)
                    .orElseThrow(() -> new ObjectNotFoundException("Avaliação com ID " + assessmentId + " não encontrada!"))
                : assessmentRepo.findByIdAndUserId(assessmentId, userId)
                    .orElseThrow(() -> new ObjectNotFoundException("Avaliação com ID " + assessmentId + " não encontrada!"));

        Patient patient = assessment.getPatient();
        boolean isMan = patient.getGender().equalsIgnoreCase("M");

        assessment.setDetails(assessmentRequest.detalhes());
        assessment.setDnaTest(assessmentRequest.testeDna());
        assessment.setExamInterest(assessmentRequest.interesseExame());
        assessment.setExamResult(assessmentRequest.resultadoExame());
        assessment.setAutismDiagnosis(assessmentRequest.diagnosticoAutismo());
        assessment.setHasSiblings(assessmentRequest.possuiIrmaos());
        assessment.setDisabilityHistory(assessmentRequest.antecedentesDeficiencia());
        assessment.setMenopauseHistory(assessmentRequest.antecedentesMenopausa());
        assessment.setAtaxiaHistory(assessmentRequest.antecedentesAtaxia());

        List<AssessmentSymptom> existingRelations = assessmentSymptomRepo.findByAssessmentId(assessmentId); //busca relacoes que ja existiam
        Map<String, AssessmentSymptom> existingMap = existingRelations.stream()
                .collect(Collectors.toMap(rel -> rel.getSymptom().getName(), rel -> rel));

        Map<String, Symptom> symptomMap = loadSymptomsByName(assessmentRequest.sintomas()); //carrega sintomas novos do request

        for (SymptomRequestDTO dto : assessmentRequest.sintomas()) {
            Symptom symptom = symptomMap.get(dto.nome());
            if (symptom != null) {
                if (existingMap.containsKey(dto.nome())) {
                    AssessmentSymptom relation = existingMap.get(dto.nome());
                    relation.setPresent(dto.presente());
                    assessmentSymptomRepo.save(relation);
                } else {
                    AssessmentSymptom relation = new AssessmentSymptom(assessment, symptom, dto.presente());
                    assessmentSymptomRepo.save(relation);
                    existingRelations.add(relation); //adiciona os sintomas novos do request com os que ja existiam nas relacoes
                }
            }
        }

        // Agora calcula o score com base em TODOS os vínculos (antigos + novos)
        BigDecimal score = calculateScore(existingRelations, isMan);

        Result result = (isMan && score.compareTo(MAN_LIMIAR) >= 0) ||
                (!isMan && score.compareTo(WOMAN_LIMIAR) >= 0)
                ? Result.TESTE_INDICADO : Result.INCONCLUSIVO;

        assessment.setScore(score);
        assessment.setResult(result);

        assessmentRepo.save(assessment);

        List<String> symptoms = symptomRepo.findSymptomsByAssessment(assessment.getId());
        return assessmentMapper.toDto(assessment, symptoms);
    }

    private BigDecimal calculateScore(AssessmentRequestDTO assessmentRequest, boolean isMan, Map<String, Symptom> symptomMap)
    {
        BigDecimal score = BigDecimal.ZERO;
        for (SymptomRequestDTO dto : assessmentRequest.sintomas()) {
            if (dto.presente()) {
                Symptom s = symptomMap.get(dto.nome());
                if (s != null) {
                    score = score.add(isMan ? s.getManScore() : s.getWomanScore());
                }
            }
        }
        return score;
    }

    private BigDecimal calculateScore(List<AssessmentSymptom> relations, boolean isMan)
    {
        BigDecimal score = BigDecimal.ZERO;
        for (AssessmentSymptom rel : relations) {
            if (rel.isPresent()) {
                Symptom s = rel.getSymptom();
                score = score.add(isMan ? s.getManScore() : s.getWomanScore());
            }
        }
        return score;
    }

    private void persistSymptoms(Assessment assessment, List<SymptomRequestDTO> sintomas, Map<String, Symptom> symptomMap) 
    {
        for (SymptomRequestDTO dto : sintomas) {
            Symptom symptom = symptomMap.get(dto.nome());
            if (symptom != null) {
                AssessmentSymptom relation = new AssessmentSymptom(assessment, symptom, dto.presente());
                assessmentSymptomRepo.save(relation);
            }
        }
    }
    
    private Map<String, Symptom> loadSymptomsByName(List<SymptomRequestDTO> sintomas) 
    {
        List<String> nomes = sintomas.stream()
                .map(SymptomRequestDTO::nome)
                .toList();
        List<Symptom> symptoms = symptomRepo.findByNameIn(nomes);
        return symptoms.stream().collect(Collectors.toMap(Symptom::getName, s -> s));
    }
    
}
