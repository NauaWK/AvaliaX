
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.entities.AssessmentSymptom;
import dev.trabalho.xfragil.entities.Symptom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentSymptomRepository extends JpaRepository<AssessmentSymptom, Integer>{
    
    Optional<AssessmentSymptom> findByAssessmentAndSymptom(Assessment a, Symptom s);
    
}
