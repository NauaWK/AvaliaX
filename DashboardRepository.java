package dev.trabalho.xfragil.repositories;

import dev.trabalho.xfragil.entities.dto.response_dto.RecentAssessmentResponseDTO;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {

    private final EntityManager entityManager;

    public DashboardRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Long countPacientes() {

        Number total = (Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM pacientes")
                .getSingleResult();

        return total.longValue();
    }

    public Long countAvaliacoes() {

        Number total = (Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM avaliacoes")
                .getSingleResult();

        return total.longValue();
    }

    @SuppressWarnings("unchecked")
    public List<RecentAssessmentResponseDTO> findRecentAssessments() {

        String sql = """
                SELECT
                    a.id_avaliacao,
                    a.id_paciente,
                    p.nome AS nome_paciente,
                    a.data_avaliacao,
                    a.score,
                    a.recomendacao
                FROM avaliacoes a
                INNER JOIN pacientes p
                    ON p.id_paciente = a.id_paciente
                ORDER BY
                    a.data_avaliacao DESC,
                    a.id_avaliacao DESC
                LIMIT 5
                """;

        List<Object[]> results = entityManager
                .createNativeQuery(sql)
                .getResultList();

        return results.stream()
                .map(row -> new RecentAssessmentResponseDTO(
                        toInteger(row[0]),
                        toInteger(row[1]),
                        row[2] != null ? row[2].toString() : null,
                        toLocalDate(row[3]),
                        toBigDecimal(row[4]),
                        row[5] != null ? row[5].toString() : null
                ))
                .toList();
    }

    private Integer toInteger(Object value) {
        return value != null ? ((Number) value).intValue() : null;
    }

    private BigDecimal toBigDecimal(Object value) {
        return value != null ? new BigDecimal(value.toString()) : null;
    }

    private LocalDate toLocalDate(Object value) {

        if (value instanceof LocalDate localDate) {
            return localDate;
        }

        if (value instanceof Date date) {
            return date.toLocalDate();
        }

        return value != null ? LocalDate.parse(value.toString()) : null;
    }
}