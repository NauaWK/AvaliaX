
package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.Symptom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SymptomRepository extends JpaRepository<Symptom, Integer>{
    
    Symptom findByName(String name);
    
    List<Symptom> findByNameIn(List<String> names);
    
    @Query(value = """
        SELECT s.descricao, COUNT(*) AS qtd
        FROM avaliacao_sintoma avs
        JOIN sintomas s ON avs.id_sintoma = s.id_sintoma
        JOIN avaliacoes a ON avs.id_avaliacao = a.id_avaliacao
        JOIN pacientes p ON a.id_paciente = p.id_paciente
        WHERE a.id_usuario = :userId AND p.sexo = :gender AND avs.presente = 1
        GROUP BY s.descricao
        ORDER BY qtd DESC
        LIMIT 1
    """, nativeQuery = true)
    List<Object[]> findTopSymptomByGender(@Param("userId") Integer userId, @Param("gender") String gender);
    
    @Query(value = """
        SELECT s.descricao, COUNT(*) AS qtd
        FROM avaliacao_sintoma avs
        JOIN sintomas s ON avs.id_sintoma = s.id_sintoma
        JOIN avaliacoes a ON avs.id_avaliacao = a.id_avaliacao
        JOIN pacientes p ON a.id_paciente = p.id_paciente
        WHERE p.sexo = :gender AND avs.presente = 1
        GROUP BY s.descricao
        ORDER BY qtd DESC
        LIMIT 1
    """, nativeQuery = true)
    List<Object[]> findTopSymptomByGenderGlobal(@Param("gender") String gender);

    @Query(value = """
        SELECT s.descricao AS nome, COUNT(*) AS qtd
        FROM avaliacao_sintoma avs
        JOIN sintomas s ON avs.id_sintoma = s.id_sintoma
        JOIN avaliacoes a ON avs.id_avaliacao = a.id_avaliacao
        WHERE a.id_usuario = :userId AND avs.presente = 1
        GROUP BY s.descricao
        ORDER BY qtd DESC
    """, nativeQuery = true)
    List<Object[]> findSymptomRanking(@Param("userId") Integer userId);
    
    @Query(value = """
        SELECT s.descricao, COUNT(*) AS qtd
        FROM avaliacao_sintoma avs
        JOIN sintomas s ON avs.id_sintoma = s.id_sintoma
        JOIN avaliacoes a ON avs.id_avaliacao = a.id_avaliacao
        WHERE avs.presente = 1
        GROUP BY s.descricao
        ORDER BY qtd DESC
    """, nativeQuery = true)
    List<Object[]> findSymptomRankingGlobal();


}
