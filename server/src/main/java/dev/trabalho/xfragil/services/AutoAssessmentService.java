
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.PatientGuardian;
import dev.trabalho.xfragil.entities.dto.assessment_dtos.AssessmentResponseDTO;
import dev.trabalho.xfragil.entities.dto.autoassessment_dtos.AutoAssessmentRequestDTO;
import dev.trabalho.xfragil.repositories.PatientGuardianRepository;
import org.springframework.stereotype.Service;

@Service
public class AutoAssessmentService {
    
    private final PatientService patientService;
    private final GuardianService guardianService;
    private final AssessmentService assessmentService;
    private final PatientGuardianRepository patientGuardianRepo;
    
    public AutoAssessmentService(PatientService patientService, 
            GuardianService guardianService, 
            AssessmentService assessmentService, 
            PatientGuardianRepository patientGuardianRepo) {
        this.patientService = patientService;
        this.guardianService = guardianService;
        this.assessmentService = assessmentService;
        this.patientGuardianRepo = patientGuardianRepo;
    }
    
    public AssessmentResponseDTO processAutoAssessment(AutoAssessmentRequestDTO request) 
    {
        Patient patient = patientService.createOrFind(request.paciente());
        Guardian guardian = guardianService.createOrFind(request.responsavel());
        AssessmentResponseDTO dto = assessmentService.addAutoAssessment(patient, request.avaliacao());
        linkPatientToGuardian(patient, guardian);
        return dto;
    }
    
    public void linkPatientToGuardian(Patient patient, Guardian guardian) {
        if (!patientGuardianRepo.existsByPatientAndGuardian(patient, guardian)) {
            PatientGuardian relation = new PatientGuardian(patient, guardian);
            patientGuardianRepo.save(relation);
        }
    }
    
}
