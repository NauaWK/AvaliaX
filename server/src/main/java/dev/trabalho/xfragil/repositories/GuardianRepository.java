
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Guardian;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GuardianRepository extends JpaRepository<Guardian, Integer>{
    
    Optional<Guardian> findByCPF(String CPF);
    
}
