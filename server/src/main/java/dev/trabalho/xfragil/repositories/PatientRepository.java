
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Patient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer>{
    
    Long countByUserIdAndActiveTrue(Integer userId);
    
    Optional<Patient> findByCPF(String CPF);
    
    Optional<Patient> findByCPFAndActiveTrue(String CPF);
    
    List<Patient> findByUserIdAndActiveTrue(Integer userId);
    
    boolean existsByCPF(String CPF);
    
}
