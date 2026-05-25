
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer>{
    
    Long countByUserId(Integer userId);
    
}
