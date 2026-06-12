
package dev.trabalho.xfragil.services;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.PatientGuardian;
import dev.trabalho.xfragil.repositories.PatientGuardianRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientGuardianService {
    
    private final PatientGuardianRepository patientGuardianRepo;

    public PatientGuardianService(PatientGuardianRepository patientGuardianRepo) {
        this.patientGuardianRepo = patientGuardianRepo;
    }
    
    public void linkPatientToGuardian(Patient patient, Guardian guardian) {
        if (!patientGuardianRepo.existsByPatientAndGuardian(patient, guardian)) {
            PatientGuardian relation = new PatientGuardian(patient, guardian);
            patientGuardianRepo.save(relation);
        }
    }
    
}
