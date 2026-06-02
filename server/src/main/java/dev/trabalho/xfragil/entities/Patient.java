
package dev.trabalho.xfragil.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pacientes")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer id;  

    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    
    @Column(name = "CPF", nullable = false, unique = true)
    private String CPF;

    @Column(name = "sexo", nullable = false, length = 1)
    private String gender;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate birthDate;
    
    @Column(name = "nome_mae", nullable = false, length = 100)
    private String momName;
    
    @Column(name = "nome_pai", nullable = true, length = 100)
    private String dadName;
    
    @Column(name = "ativo", nullable = false)
    private boolean active;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private Users user;

    @OneToMany(mappedBy = "patient")
    private List<Assessment> assessments;
    
    @PrePersist
    @PreUpdate
    private void normalizeCpf() {
        if (CPF != null) {
            CPF = CPF.replaceAll("\\D", ""); //remove tudo que não é número
        }
    }
    
    public Patient() {}

    public Patient(String name, 
            String CPF, 
            String gender, 
            LocalDate birthDate, 
            String momName, 
            String dadName, 
            Users user) {
        this.name = name;
        this.CPF = CPF;
        this.gender = gender;
        this.birthDate = birthDate;
        this.momName = momName;
        this.dadName = dadName;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getMomName() {
        return momName;
    }

    public void setMomName(String momName) {
        this.momName = momName;
    }

    public String getDadName() {
        return dadName;
    }

    public void setDadName(String dadName) {
        this.dadName = dadName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

}

