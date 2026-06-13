
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Assessment;
import dev.trabalho.xfragil.utils.enums.Result;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssessmentRepository extends JpaRepository<Assessment, Integer>{
    
    Long countByUserId(Integer userId);

    Long countByUserIdAndResult(Integer userId, Result result);
    
    Long countByPatientIdAndResult(Integer patientId, Result result);
    
    @Query(value = "SELECT AVG(score) from avaliacoes "
            + "WHERE id_usuario = :userId", nativeQuery = true)
    Double findAverageScoreByUserId(@Param("userId") Integer userId);
    
    @Query(value = "SELECT * FROM avaliacoes "
            + "WHERE id_usuario = :userId "
            + "OR origem = 'RESPONSAVEL'", 
            nativeQuery = true)
    List<Assessment> findByUserId(@Param("userId") Integer userId);

    Optional<Assessment> findByIdAndUserId(Integer id, Integer userId);
    
        @Query(value = "SELECT * FROM avaliacoes "
            + "WHERE id_usuario = :userId "
            + "ORDER BY data_avaliacao "
            + "DESC LIMIT 5", nativeQuery = true)
    List<Assessment> findRecentAssessmentsByUserId(@Param("userId") Integer userId);
    
    Optional<Assessment> findTopByPatientIdOrderByAssessmentDateDesc(Integer patientId);
    
    List<Assessment> findTop3ByPatientIdOrderByAssessmentDateDesc(Integer patientId);
    
    @Query(value = "SELECT COUNT(*) FROM avaliacoes a "
            + "WHERE a.id_paciente = :patientId", nativeQuery = true)
    Long countByPatient(@Param("patientId") Integer patientId);
    
    @Query(value = "SELECT COUNT(*) FROM avaliacoes a "
            + "WHERE a.id_paciente = :patientId AND a.origem = 'PROFISSIONAL'", nativeQuery = true)
    Long countProfissionalAssessmentByPatient(@Param("patientId") Integer patientId);

    @Query(value = "SELECT COUNT(*) FROM avaliacoes a "
            + "WHERE a.id_paciente = :patientId AND a.origem = 'RESPONSAVEL'", nativeQuery = true)
    Long countGuardianAssessmentsByPatient(@Param("patientId") Integer patientId);

    @Query(value = "SELECT AVG(a.score) FROM avaliacoes a "
            + "WHERE a.id_paciente = :patientId", nativeQuery = true)
    Double findAvgScoreByPatient(@Param("patientId") Integer patientId);

    @Query(value = """
        SELECT s.descricao, COUNT(*) AS qtd
        FROM avaliacoes a
        JOIN avaliacao_sintoma avs ON a.id_avaliacao = avs.id_avaliacao
        JOIN sintomas s ON s.id_sintoma = avs.id_sintoma
        WHERE a.id_paciente = :patientId AND avs.presente = 1
        GROUP BY s.descricao
        ORDER BY qtd DESC
        """, nativeQuery = true)
    List<Object[]> findSymptomRankingByPatient(@Param("patientId") Integer patientId);
    
    
    //<----- esses métodos são para o admin ----->
    Long countByResult(Result result); 
    
    @Query(value = "SELECT * FROM avaliacoes "
            + "ORDER BY data_avaliacao "
            + "DESC LIMIT 5", nativeQuery = true)
    List<Assessment> findRecentAssessments();
    
    @Query(value = "SELECT AVG(score) FROM avaliacoes", nativeQuery = true)
    Double findAverageScoreGlobal();
    
}
