
package dev.trabalho.xfragil.entities;

import dev.trabalho.xfragil.utils.enums.Result;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avaliacoes")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Users user; 

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate assessmentDate;

    @Column(name = "score", precision = 4, scale = 2)
    private BigDecimal score;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", nullable = false)
    private Result result;
    
    @Column(name = "detalhes", nullable = true)
    private String details;

    public Assessment() {}

    public Assessment(Patient patient, Users user, BigDecimal score, Result result, String details) {
        this.patient = patient;
        this.user = user;
        this.score = score;
        this.result = result;
        this.details = details;
    }
    
    @PrePersist
    private void setAssessmentDate() {
        this.assessmentDate = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}

