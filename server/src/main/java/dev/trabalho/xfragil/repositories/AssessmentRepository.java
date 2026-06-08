
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.utils.enums.Result;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssessmentRepository extends JpaRepository<Assessment, Integer>{
    
    Long countByUserId(Integer userId);

    Long countByUserIdAndResult(Integer userId, Result result);
    
    @Query(value = "SELECT AVG(score) from avaliacoes WHERE id_usuario = :userId", nativeQuery = true)
    Double findAverageScoreByUserId(@Param("userId") Integer userId);
    
    @Query(value = "SELECT * FROM avaliacoes "
            + "WHERE id_usuario = :userId "
            + "OR origem = 'RESPONSAVEL'", 
            nativeQuery = true)
    List<Assessment> findByUserId(@Param("userId") Integer userId);
    
        @Query(value = "SELECT * FROM avaliacoes "
            + "WHERE id_usuario = :userId "
            + "ORDER BY data_avaliacao "
            + "DESC LIMIT 5", nativeQuery = true)
    List<Assessment> findRecentAssessmentsByUserId(@Param("userId") Integer userId);
    
    //<-- esses métodos são para o admin -->
    Long countByResult(Result result); 
    
    @Query(value = "SELECT * FROM avaliacoes "
            + "ORDER BY data_avaliacao "
            + "DESC LIMIT 5", nativeQuery = true)
    List<Assessment> findRecentAssessments();
    
    @Query(value = "SELECT AVG(score) from avaliacoes", nativeQuery = true)
    Double findAverageScoreGlobal();
    
}
