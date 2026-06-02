package dev.trabalho.xfragil.entities;

import dev.trabalho.xfragil.utils.enums.Answer;
import dev.trabalho.xfragil.utils.enums.ExamResult;
import dev.trabalho.xfragil.utils.enums.Origin;
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
    @JoinColumn(name = "id_usuario", nullable = true)
    private Users user; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "origem", nullable = false)
    private Origin origin;
    
    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate assessmentDate;

    @Column(name = "score", precision = 4, scale = 2)
    private BigDecimal score;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", nullable = false)
    private Result result;
    
    @Column(name = "detalhes", length = 255, nullable = false)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "exame_dna", nullable = false)
    private Answer dnaTest;

    @Enumerated(EnumType.STRING)
    @Column(name = "interesse_exame", nullable = true)
    private Answer examInterest;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_exame", nullable = true)
    private ExamResult examResult;

    @Enumerated(EnumType.STRING)
    @Column(name = "diagnostico_autismo", nullable = false)
    private Answer autismDiagnosis;

    @Enumerated(EnumType.STRING)
    @Column(name = "possui_irmaos", nullable = false)
    private Answer hasSiblings;

    @Enumerated(EnumType.STRING)
    @Column(name = "antecedentes_deficiencia", nullable = false)
    private Answer disabilityHistory;

    @Enumerated(EnumType.STRING)
    @Column(name = "antecedentes_menopausa", nullable = false)
    private Answer menopauseHistory;

    @Enumerated(EnumType.STRING)
    @Column(name = "antecedentes_ataxia", nullable = false)
    private Answer ataxiaHistory;

    public Assessment() {}

    public Assessment(Patient patient, 
            Users user, 
            Origin origin, 
            BigDecimal score, 
            Result result) {
        this.patient = patient;
        this.user = user;
        this.origin = origin;
        this.score = score;
        this.result = result;
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

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
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

    public Answer getDnaTest() {
        return dnaTest;
    }

    public void setDnaTest(Answer dnaTest) {
        this.dnaTest = dnaTest;
    }

    public Answer getExamInterest() {
        return examInterest;
    }

    public void setExamInterest(Answer examInterest) {
        this.examInterest = examInterest;
    }

    public ExamResult getExamResult() {
        return examResult;
    }

    public void setExamResult(ExamResult examResult) {
        this.examResult = examResult;
    }

    public Answer getAutismDiagnosis() {
        return autismDiagnosis;
    }

    public void setAutismDiagnosis(Answer autismDiagnosis) {
        this.autismDiagnosis = autismDiagnosis;
    }

    public Answer getHasSiblings() {
        return hasSiblings;
    }

    public void setHasSiblings(Answer hasSiblings) {
        this.hasSiblings = hasSiblings;
    }

    public Answer getDisabilityHistory() {
        return disabilityHistory;
    }

    public void setDisabilityHistory(Answer disabilityHistory) {
        this.disabilityHistory = disabilityHistory;
    }

    public Answer getMenopauseHistory() {
        return menopauseHistory;
    }

    public void setMenopauseHistory(Answer menopauseHistory) {
        this.menopauseHistory = menopauseHistory;
    }

    public Answer getAtaxiaHistory() {
        return ataxiaHistory;
    }

    public void setAtaxiaHistory(Answer ataxiaHistory) {
        this.ataxiaHistory = ataxiaHistory;
    }

}
