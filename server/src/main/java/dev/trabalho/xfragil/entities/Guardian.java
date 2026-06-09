
package dev.trabalho.xfragil.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "responsaveis")
public class Guardian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsavel")
    private Integer id;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    
    @Column(name = "CPF", nullable = false, unique = true)
    private String CPF;

    @Column(name = "grau_parentesco", nullable = false, length = 50)
    private String relationshipDegree;

    @Column(name = "cidade", nullable = false, length = 100)
    private String city;
     
    @Column(name = "estado", nullable = false, length = 100)
    private String state;
      
    @Column(name = "pais", nullable = false, length = 100)
    private String country;
    
    @Column(name = "whatsapp", nullable = true, length = 20)
    private String whatsapp;
    
    @Column(name = "telefone1", nullable = false, length = 20)
    private String telefone1;
    
    @Column(name = "telefone2", nullable = true, length = 20)
    private String telefone2;
    
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    public Guardian() {}

    public Guardian(String name, String CPF, String relationshipDegree, String city, String state, String country, String whatsapp, String telefone1, String telefone2, String email) {
        this.name = name;
        this.CPF = CPF;
        this.relationshipDegree = relationshipDegree;
        this.city = city;
        this.state = state;
        this.country = country;
        this.whatsapp = whatsapp;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;
        this.email = email;
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

    public String getRelationshipDegree() {
        return relationshipDegree;
    }

    public void setRelationshipDegree(String relationshipDegree) {
        this.relationshipDegree = relationshipDegree;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
