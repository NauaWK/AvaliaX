
package dev.trabalho.xfragil.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "paciente_responsavel")
public class PatientGuardian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_responsavel")
    private Guardian guardian;

    public PatientGuardian() {}

    public PatientGuardian(Patient patient, Guardian guardian) {
        this.patient = patient;
        this.guardian = guardian;
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

    public Guardian getGuardian() {
        return guardian;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }
    
}

