
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Symptom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SymptomRepository extends JpaRepository<Symptom, Integer>{
    
    Symptom findByName(String name);
    
    List<Symptom> findByNameIn(List<String> names);
    
}
