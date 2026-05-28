
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.AssessmentSymptom;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.Symptom;
import dev.trabalho.xfragil.entities.Users;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.entities.dto.symptom_dto.SymptomRequestDTO;
import dev.trabalho.xfragil.repositories.AssessmentRepository;
import dev.trabalho.xfragil.repositories.AssessmentSymptomRepository;
import dev.trabalho.xfragil.repositories.SymptomRepository;
import dev.trabalho.xfragil.utils.enums.Result;
import dev.trabalho.xfragil.utils.mappers.AssessmentMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {
    
    private final AssessmentRepository assessmentRepo;
    private final AssessmentMapper assessmentMapper;
    private final PatientService patientService;
    private final UserService userService;
    private final SymptomRepository symptomRepo;
    private final AssessmentSymptomRepository assessmentSymptomRepo;

    public AssessmentService(AssessmentRepository assessmentRepo, 
            AssessmentMapper assessmentMapper, 
            PatientService patientService, 
            UserService userService, 
            SymptomRepository symptomRepo, 
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
        
        List<AssessmentResponseDTO> dtos = assessments.stream()
               .map(assessmentMapper::toDto)
               .toList();
        
        return dtos;
    }
    
    public List<AssessmentResponseDTO> getAssessmentsByUserId(Integer userId)
    {
        List<Assessment> assessments = assessmentRepo.findByUserId(userId);
        
        List<AssessmentResponseDTO> dtos = assessments.stream()
               .map(assessmentMapper::toDto)
               .toList();
        
        return dtos;
    }
    
    public AssessmentResponseDTO addAssessment(AssessmentRequestDTO assessmentRequest, Integer userId)
    {
        Patient patient = patientService.findPatientById(assessmentRequest.id_paciente());
        Users user = userService.findUserByUserId(userId);
        
        boolean isMan = patient.getGender().equalsIgnoreCase("M");
        final BigDecimal WOMAN_LIMIAR = new BigDecimal("0.55");
        final BigDecimal MAN_LIMIAR = new BigDecimal("0.56");
        
        BigDecimal score = calculateScore(assessmentRequest, isMan);
        Result result = Result.INCONCLUSIVO;
        
        if(isMan && score.compareTo(MAN_LIMIAR) >= 0) result = Result.TESTE_INDICADO;
        else if (!isMan && score.compareTo(WOMAN_LIMIAR) >= 0) result = Result.TESTE_INDICADO;
        
        Assessment assessment = assessmentMapper.toAssessment(patient, user, score, result, assessmentRequest.detalhes());
        assessmentRepo.save(assessment);
        
        persistSymptoms(assessment, assessmentRequest.sintomas());
        
        return assessmentMapper.toDto(assessment);
    }
    
    private BigDecimal calculateScore(AssessmentRequestDTO assessmentRequest, boolean isMan)
    {
        BigDecimal score = BigDecimal.ZERO;
        for(SymptomRequestDTO symptomDto : assessmentRequest.sintomas()){
            if(symptomDto.presente()){
                Symptom s = symptomRepo.findByName(symptomDto.nome());
                if(isMan) score = score.add(s.getManScore());
                else score = score.add(s.getWomanScore());
            }
        }
        return score;
    }
    
    private void persistSymptoms(Assessment assessment, List<SymptomRequestDTO> sintomas) {
        for (SymptomRequestDTO dto : sintomas) {
            Symptom symptom = symptomRepo.findByName(dto.nome());
            AssessmentSymptom relation = new AssessmentSymptom(assessment, symptom, dto.presente());
            assessmentSymptomRepo.save(relation);
        }
    }
    
}
