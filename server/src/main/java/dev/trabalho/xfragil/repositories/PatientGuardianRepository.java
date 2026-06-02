package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Guardian;
import dev.trabalho.xfragil.entities.Patient;
import dev.trabalho.xfragil.entities.PatientGuardian;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientGuardianRepository extends JpaRepository<PatientGuardian, Integer>{
    
    boolean existsByPatientAndGuardian(Patient p, Guardian g);
    
}
