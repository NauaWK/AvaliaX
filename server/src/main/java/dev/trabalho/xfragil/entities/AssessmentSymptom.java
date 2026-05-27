
package dev.trabalho.xfragil.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "avaliacao_sintoma")
public class AssessmentSymptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_avaliacao", nullable = false)
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "id_sintoma", nullable = false)
    private Symptom symptom;

    @Column(name = "presente", nullable = false)
    private boolean present;

    public AssessmentSymptom() {}

    public AssessmentSymptom(Assessment assessment, Symptom symptom, boolean present) {
        this.assessment = assessment;
        this.symptom = symptom;
        this.present = present;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Symptom getSymptom() {
        return symptom;
    }

    public void setSymptom(Symptom symptom) {
        this.symptom = symptom;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

}

