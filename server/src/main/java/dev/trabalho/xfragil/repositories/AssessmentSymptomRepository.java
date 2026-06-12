
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.AssessmentSymptom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentSymptomRepository extends JpaRepository<AssessmentSymptom, Integer>{

    List<AssessmentSymptom> findByAssessmentId(Integer id);

}
