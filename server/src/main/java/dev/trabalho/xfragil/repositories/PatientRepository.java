
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Patient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer>{
    
    Long countByUserId(Integer userId);
    
    List<Patient> findByUserId(Integer userId);
    
    Optional<Patient> findByCPF(String CPF);
    
    boolean existsByNameOrCPF(String name, String CPF);
    
}
