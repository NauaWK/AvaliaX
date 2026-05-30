
package dev.trabalho.xfragil.entities;

import jakarta.persistence.*;
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

    @Column(name = "idade")
    private Integer age;
    
    @Column(name = "ativo", nullable = false)
    private boolean active;

    @Column(name = "responsavel", length = 100)
    private String guardian;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private Users user;

    @OneToMany(mappedBy = "patient")
    private List<Assessment> assessments;

    public Patient() {}

    public Patient(String name, String CPF, String gender, Integer age, String guardian, Users user) {
        this.name = name;
        this.CPF = CPF;
        this.gender = gender;
        this.age = age;
        this.guardian = guardian;
        this.user = user;
    }
    
    @PrePersist
    @PreUpdate
    private void normalizeCpf() {
        if (CPF != null) {
            CPF = CPF.replaceAll("\\D", ""); //remove tudo que não é número
        }
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
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

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}

