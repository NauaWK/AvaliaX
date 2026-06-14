
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Patient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient, Integer>{
    
    
    @Query(value = "SELECT COUNT(*) FROM pacientes " +
               "WHERE (id_usuario = :userId OR id_usuario IS NULL) " +
               "AND ativo = true", 
       nativeQuery = true)
    Long countByUserId(@Param("userId") Integer userId);
    
    Optional<Patient> findByCPF(String CPF);
    
    Optional<Patient> findByCPFAndActiveTrue(String CPF);
    
    @Query(value = "SELECT * FROM pacientes " +
               "WHERE (id_usuario = :userId OR id_usuario IS NULL) " +
               "AND ativo = true", 
       nativeQuery = true)
    List<Patient> findByUserIdAndActiveTrue(@Param("userId") Integer userId);
    
    boolean existsByCPF(String CPF);
    
}
