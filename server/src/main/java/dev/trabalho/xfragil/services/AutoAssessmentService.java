
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentRequestDTO;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.entities.dto.autoassessment_dtos.AutoAssessmentRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class AutoAssessmentService {
    
    private final PatientService patientService;
    private final GuardianService guardianService;
    private final AssessmentService assessmentService;
    private final PatientGuardianService patientGuardianService;

    public AutoAssessmentService(PatientService patientService, GuardianService guardianService, AssessmentService assessmentService, PatientGuardianService patientGuardianService) {
        this.patientService = patientService;
        this.guardianService = guardianService;
        this.assessmentService = assessmentService;
        this.patientGuardianService = patientGuardianService;
    }
    
    public AssessmentResponseDTO processAutoAssessment(AutoAssessmentRequestDTO request) 
    {
        Patient patient = patientService.createOrFind(request.paciente());
        Guardian guardian = guardianService.createOrFind(request.responsavel());
        
        //força o CPF da avaliação a ser o mesmo do paciente
        AssessmentRequestDTO correctedAssessmentDto = new AssessmentRequestDTO(
            patient.getCPF(),
            request.avaliacao().detalhes(),
            request.avaliacao().testeDna(),
            request.avaliacao().interesseExame(),
            request.avaliacao().resultadoExame(),
            request.avaliacao().diagnosticoAutismo(),
            request.avaliacao().possuiIrmaos(),
            request.avaliacao().antecedentesDeficiencia(),
            request.avaliacao().antecedentesMenopausa(),
            request.avaliacao().antecedentesAtaxia(),
            request.avaliacao().sintomas()
        );
        
        patientGuardianService.linkPatientToGuardian(patient, guardian);
        return assessmentService.addAutoAssessment(patient, correctedAssessmentDto);
    }
    
}
